import tensorflow as tf
import glob
import numpy as np
import datetime

from tensorflow.contrib.session_bundle import exporter

#Log Start Time to help show duration
startTime = datetime.datetime.now()

sess = tf.Session()
tf.logging.set_verbosity(tf.logging.INFO)

# First we want to build our index of classes
labelGlob = glob.glob("101_ObjectCategories/*")
labelMaster = np.ndarray(shape=(47), dtype=np.dtype('S20'))

loop = 0
for folder in labelGlob:
    labelKey = folder.split("/")[1]
    labelMaster.itemset(loop, labelKey)
    loop = loop + 1

# print("labelMaster: ", labelMaster)
print("Number of Classes: ", len(labelMaster))

# Get the images we need to process
files = glob.glob("101_ObjectCategories/*/*.jpg")

print("Number of Images: ", len(files))
print("")

#Create the empty arrays we need to fill
allImages = np.ndarray(shape=(3746,90000))
allLabels = np.ndarray(shape=(3746,47))

for loop in range(0, len(files)):
#for loop in range(0, 300):
    curFile = files[loop]
    curLabel = files[loop].split("/")[1]

    #Build our label matrix
    indexOf = np.where(labelMaster == str.encode(curLabel))
    print("class: ", curLabel, "  index: ", indexOf[0], "  fileName: ", curFile)

    curLabels = np.zeros(shape=(47))
    curLabels[indexOf[0]] = 1
    allLabels[loop] = curLabels

    #Build the pixel map
    file_contents = tf.read_file(files[loop])
    readImage = tf.image.decode_jpeg(file_contents, channels=1)
    image = tf.image.resize_images(readImage, [300, 300])

    with sess.as_default():
        flatImage = image.eval().ravel()
    flatImage = np.multiply(flatImage, 1.0 / 255.0)
    #print("Flat Image: ", flatImage)
    allImages[loop] = flatImage

print ("Length of allImages: ", len(allImages))
print ("Length of allLabels: ", len(allLabels))

#Our Data is now finished loading, we need to split it up

#Begin setting up our Tensorflow approach
x = tf.placeholder(tf.float32, [None, 90000],name='x')
W = tf.Variable(tf.zeros([90000, 47]),name='W')
b = tf.Variable(tf.zeros([47]),name='b')

y = tf.nn.softmax(tf.matmul(x, W) + b,name='y')
y_ = tf.placeholder(tf.float32, [None, 47],name='y_')
tf.add_to_collection('variables',W)
tf.add_to_collection('variables',b)

cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=y, labels=y_))

train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

# save summaries for visualization
tf.summary.histogram('weights', W)
tf.summary.histogram('max_weight', tf.reduce_max(W))
tf.summary.histogram('bias', b)
tf.summary.scalar('cross_entropy', cross_entropy)
tf.summary.histogram('cross_hist', cross_entropy)

# merge all summaries into one op
merged=tf.summary.merge_all()

trainwriter=tf.summary.FileWriter('data/model'+'/logs/train',sess.graph)

init = tf.global_variables_initializer()
sess.run(init)

for i in range(200):
    #Generate our data sample.  Let's use 5% of data
    print("Running Iteration ", i)
    mask = np.random.choice([True, False], len(allImages), p=[0.05, 0.95])
    trainImages = allImages[mask]
    trainLabels = allLabels[mask]
    summary, _ = sess.run([merged, train_step], feed_dict={x: trainImages, y_: trainLabels})
    trainwriter.add_summary(summary, i)

endModelTime = datetime.datetime.now()

# model export path
export_path = 'data/model'
print('Exporting trained model to', export_path)

saver = tf.train.Saver(sharded=True)
model_exporter = exporter.Exporter(saver)
model_exporter.init(
    sess.graph.as_graph_def(),
    named_graph_signatures={
        'inputs': exporter.generic_signature({'images': x}),
        'outputs': exporter.generic_signature({'scores': y})})

model_exporter.export(export_path, tf.constant(1), sess)

print("Program Complete!")
print("Start Time:     ", startTime)
print("Model Complete: ", endModelTime)
print("End Time:       ", datetime.datetime.now())
