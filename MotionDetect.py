import random as rng
import argparse
import cv2 as cv
import numpy as np

def nothing(x):
    pass

# TODO: 1) Capture an image
cap = cv.VideoCapture(0)
cv.namedWindow("Video")
status, frame = cap.read()

# TODO: 2) Create blank image
width, height, channels = frame.shape

# TODO: 2a. Grayscale Image with proper dimensions
src = cv.cvtColor(frame, cv.COLOR_BGR5652GRAY)
cv.imshow("Grayscale", src)

bw = np.zeros((width, height, 1), np.uint8)
cv.imshow("BW", bw)

# TODO: 2b. 32f, 3 channel image
imageForAve = np.zeros((width, height, 3), np.float32)

# TODO: 2c. a capture clone called image1
image1 = frame.copy()

# TODO: 2d. Absolute difference function
diff = cv.absdiff(frame, image1)
status, src = cap.read()
cv.imshow("Video", src)

# TODO: 3) while loop to capture images
while True:


# TODO: 4) grab new frame
# TODO: brighten image a bit
    bright = 35 * np.ones((width, height, 3), np.uint8)
# TODO: 5) blue the image
# frame = cv.blur(frame)

# TODO: 6) take the running average of frame
# cv.accumulateWeighted(frame, frame)
# TODO: 7) swap running average results from step 6 to same bits as frame:

# TODO: 8) Take difference, built in OpenCV function to do a diff between two images
# TODO: 9) Convert to grayscale

# TODO: 10) Threshold grayscale (using low numbers)
# TODO: 11) Blur grayscale image
# TODO: 12) Threshold grayscale again (using high numbers)
# TODO: 13) find contours
    if cv.waitKey(1) & 0xFF == ord("q"):
        cv.destroyAllWindows()
        break