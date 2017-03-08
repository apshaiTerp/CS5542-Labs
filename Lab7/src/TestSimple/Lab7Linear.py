from __future__ import print_function
import tensorflow as tf
import numpy
import matplotlib.pyplot as plt
import pandas as pd

from sklearn.datasets import load_boston
from sklearn.decomposition import PCA
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression

rng = numpy.random

dataset = load_boston()

print("Boston Dataset Keys")
print(dataset.keys())
print("")

print(dataset.data.shape)
print(dataset.target.shape)

df = pd.DataFrame(dataset.data, columns=dataset.feature_names)
df['target'] = dataset.target

print(df.describe())
print("")

data_reduced=PCA(n_components=1).fit_transform(dataset.data)

X_train, X_test, y_train, y_test = train_test_split(data_reduced, dataset.target)

print(X_train.shape, X_test.shape, y_train.shape, y_test.shape)

linear = LinearRegression().fit(X_train, y_train)
y_pred = linear.predict(X_test)

print("R-squared for train: %.2f" %linear.score(X_train, y_train))
print("R-squared for test:  %.2f" %linear.score(X_test, y_test))
print("")

print("Coefficients (Parameters theta_1..theta_n")
print(linear.coef_)
print("Y intercept (theta_0): %.2f" %linear.intercept_)
print("")

plt.scatter(data_reduced, dataset.target, c='r')
plt.plot(X_test, y_pred, '--k', c='b')
plt.show()
