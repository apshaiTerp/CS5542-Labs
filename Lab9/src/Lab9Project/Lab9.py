import tensorflow as tf
import glob
import numpy as np
import datetime

from tensorflow.contrib.session_bundle import exporter

#Log Start Time to help show duration
startTime = datetime.datetime.now()

sess = tf.InteractiveSession()
tf.logging.set_verbosity(tf.logging.INFO)

# First we want to build our index of classes
labelGlob = glob.glob("dataset3/train/*")
labelMaster = np.ndarray(shape=(9), dtype=np.dtype('S20'))

loop = 0
for folder in labelGlob:
    labelKey = folder.split("/")[2]
    labelMaster.itemset(loop, labelKey)
    loop = loop + 1

# print("labelMaster: ", labelMaster)
print("Number of Classes: ", len(labelMaster))

# Get the images we need to process
files = glob.glob("dataset3/train/*/*.jpg")

testFiles = glob.glob("dataset3/test/*/*.jpg")

print("Number of Images: ", len(files))
print("")

#Create the empty arrays we need to fill
allImages = np.ndarray(shape=(1350,40000))
allLabels = np.ndarray(shape=(1350,9))

testImages = np.ndarray(shape=(90,40000))
testLabels = np.ndarray(shape=(90,9))

for loop in range(0, len(files)):
#for loop in range(0, 300):
    curFile = files[loop]
    curLabel = files[loop].split("/")[2]

    #Build our label matrix
    indexOf = np.where(labelMaster == str.encode(curLabel))
    print("class: ", curLabel, "  index: ", indexOf[0], "  fileName: ", curFile)

    curLabels = np.zeros(shape=(9))
    curLabels[indexOf[0]] = 1
    allLabels[loop] = curLabels

    #Build the pixel map
    file_contents = tf.read_file(files[loop])
    readImage = tf.image.decode_jpeg(file_contents, channels=1)
    image = tf.image.resize_images(readImage, [200, 200])

    with sess.as_default():
        flatImage = image.eval().ravel()
    flatImage = np.multiply(flatImage, 1.0 / 255.0)
    #print("Flat Image: ", flatImage)
    allImages[loop] = flatImage

print ("Length of allImages: ", len(allImages))
print ("Length of allLabels: ", len(allLabels))

# Load our test data
for loop in range(0, len(testFiles)):
#for loop in range(0, 300):
    curFile = testFiles[loop]
    curLabel = testFiles[loop].split("/")[2]

    #Build our label matrix
    indexOf = np.where(labelMaster == str.encode(curLabel))
    print("class: ", curLabel, "  index: ", indexOf[0], "  fileName: ", curFile)

    curLabels = np.zeros(shape=(9))
    curLabels[indexOf[0]] = 1
    testLabels[loop] = curLabels

    #Build the pixel map
    file_contents = tf.read_file(testFiles[loop])
    readImage = tf.image.decode_jpeg(file_contents, channels=1)
    image = tf.image.resize_images(readImage, [200, 200])

    with sess.as_default():
        flatImage = image.eval().ravel()
    flatImage = np.multiply(flatImage, 1.0 / 255.0)
    #print("Flat Image: ", flatImage)
    testImages[loop] = flatImage
#Our Data is now finished loading, we need to split it up

#Begin setting up our Tensorflow approach
x = tf.placeholder(tf.float32, [None, 40000],name='x')
y_ = tf.placeholder(tf.float32, [None, 9],name='y_')

init = tf.global_variables_initializer()
sess.run(init)


def weight_variable(shape):
    initial = tf.truncated_normal(shape, stddev=0.1)
    return tf.Variable(initial)

def bias_variable(shape):
    initial = tf.constant(0.1, shape=shape)
    return tf.Variable(initial)

def conv2d(x, W):
    return tf.nn.conv2d(x, W, strides=[1, 1, 1, 1], padding='SAME')

def max_pool_2x2(x):
    return tf.nn.max_pool(x, ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1], padding='SAME')

W_conv1 = weight_variable([5, 5, 1, 32])
b_conv1 = bias_variable([32])
x_image = tf.reshape(x, [-1, 200, 200, 1])
h_conv1 = tf.nn.relu(conv2d(x_image, W_conv1) + b_conv1)
h_pool1 = max_pool_2x2(h_conv1)


W_conv2 = weight_variable([5, 5, 32, 64])
b_conv2 = bias_variable([64])

h_conv2 = tf.nn.relu(conv2d(h_pool1, W_conv2) + b_conv2)
h_pool2 = max_pool_2x2(h_conv2)

W_fc1 = weight_variable([50 * 50 * 64, 1024])
b_fc1 = bias_variable([1024])

h_pool2_flat = tf.reshape(h_pool2, [-1, 50 * 50 * 64])
h_fc1 = tf.nn.relu(tf.matmul(h_pool2_flat, W_fc1) + b_fc1)
# Dropout
keep_prob = tf.placeholder(tf.float32)
h_fc1_drop = tf.nn.dropout(h_fc1, keep_prob)
# readout layer
W_fc2 = weight_variable([1024, 9])
b_fc2 = bias_variable([9])

y_conv = tf.matmul(h_fc1_drop, W_fc2) + b_fc2
cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=y_conv, labels=y_))

train_step = tf.train.AdamOptimizer(1e-4).minimize(cross_entropy)
correct_prediction = tf.equal(tf.argmax(y_conv, 1), tf.argmax(y_, 1))
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

tf.summary.scalar('cross_entropy', cross_entropy)
tf.summary.histogram('cross_hist', cross_entropy)
merged = tf.summary.merge_all()
trainwriter = tf.summary.FileWriter('data/logs', sess.graph)
sess.run(tf.global_variables_initializer())

for i in range(100):
    #Generate our data sample.  Let's use 5% of data
    print("", datetime.datetime.now(), "Running Iteration ", i)
    mask = np.random.choice([True, False], len(allImages), p=[0.10, 0.90])
    trainImages = allImages[mask]
    trainLabels = allLabels[mask]
    # print(datetime.datetime.now(), "  Generated data mask.  About to run the training step...")
    summary, _ = sess.run([merged, train_step], feed_dict={x: trainImages, y_: trainLabels, keep_prob: 0.5})
    print(datetime.datetime.now(), "  Training Step complete.  Log summary...")
    trainwriter.add_summary(summary, i)
    if i % 25 == 0:
        train_accuracy = accuracy.eval(feed_dict={x: trainImages, y_: trainLabels, keep_prob: 1.0})
        print("step %d, training accuracy %g" % (i, train_accuracy))


print("test accuracy %g" % accuracy.eval(feed_dict={x: testImages, y_: testLabels, keep_prob: 1.0}))


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
        'outputs': exporter.generic_signature({'scores': y_})})

model_exporter.export(export_path, tf.constant(1), sess)

print("Program Complete!")
print("Start Time:     ", startTime)
print("Model Complete: ", endModelTime)
print("End Time:       ", datetime.datetime.now())
