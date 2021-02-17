import random as rng
import argparse
import cv2 as cv
import numpy as np

def nothing(x):
    pass

# TODO: 1) Capture an image
cap = cv.VideoCapture(0)
status, frame = cap.read()

# TODO: 2) Create blank image
width, height, channels = frame.shape
# TODO: 2a. Grayscale Image with proper dimensions
bw = np.zeros((width, height, 1), np.uint8)
# TODO: 2b. 32f, 3 channel image
imageForAve = np.zeros((width, height, 3), np.float32)

# used to brighten the image
bright = 35 * np.ones((width, height, 3), np.uint8)

# TODO: 3) while loop to capture images
while True:
    # TODO: 4) grab new frame
    status, frame = cap.read()
    image1 = frame.copy()

    # TODO: brighten image a bit
    # frame = frame*bright

    # TODO: 5) blur the image
    frame = cv.blur(frame, (21, 21), 0)

    # TODO: 6) take the running average of frame
    accweight = cv.accumulateWeighted(frame, imageForAve, .04)

    # TODO: 7) swap running average results from step 6 to same bits as frame:
    avg = cv.convertScaleAbs(imageForAve)

    # TODO: 8) Take difference, built in OpenCV function to do a diff between two images
    diff = cv.absdiff(frame, avg)

    # TODO: 9) Convert to grayscale
    gray = cv.cvtColor(diff, cv.COLOR_BGR2GRAY)
    cv.imshow("Grayscale", gray)

    # TODO: 10) Threshold grayscale (using low numbers)
    _, bw = cv.threshold(gray, 25, 255, cv.THRESH_BINARY)
    cv.imshow("Threshold Low", bw)

    # TODO: 11) Blur grayscale image
    # gray = cv.blur(bw, (21, 21), 0)

    # TODO: 12) Threshold grayscale again (using high numbers)
    _, bw = cv.threshold(gray, 75, 255, cv.THRESH_BINARY)
    cv.imshow("Threshold High", bw)

    # TODO: 13) find contours
    contours, _ = cv.findContours(bw, cv.RETR_CCOMP, cv.CHAIN_APPROX_SIMPLE)
    # cv.drawContours(frame, contours, -1, (0, 255, 0), 2)

    for contour in contours:
        if cv.contourArea(contour) < 10000:
            (x, y, w, h) = cv.boundingRect(contour)
            cv.rectangle(image1, (x, y), (x+w, y+h), (0, 255, 0), 3)

    cv.imshow("Contours", image1)

    if cv.waitKey(1) & 0xFF == ord("q"):
        cv.destroyAllWindows()
        break

cap.release()