import random as rng
import argparse
import cv2 as cv
import numpy as np
from numpy.core.shape_base import _accumulate

cap = cv.VideoCapture(0)
status, frame = cap.read()
width, height, channels = frame.shape
bw = np.zeros((width, height, 1), np.uint8)
imageForAve = np.zeros((width, height, 3), np.float32)
bright = 35 * np.ones((width, height, 3), np.uint8)
runningColorDepth = frame.copy()
difference = frame.copy()

# TODO: 1) Capture an image
# TODO: 2) Create blank image
# TODO: 1. Grayscale Image with proper dimensions

# TODO: 2. 32f, 3 channel image
# TODO: 3. a capture clone called image1
# TODO: 4. Absolute difference function
# TODO: 3) while loop to capture images
# TODO: 4) grab new frame
# TODO: brighten image a bit
# TODO: 5) blue the image
# TODO: 6) take the running average of frame
def accumulateWeight() :
    return
# TODO: 7) swap running average results from step 6 to same bits as frame:
def convertScaleAbs() :
    return
# TODO: 8) Take difference, built in OpenCV function to do a diff between two images
# TODO: 9) Convert to grayscale
def grayscaleConverter(src) :
    src = cv.imread("japaneseflowers.jpg", 1)
    if src is None:
        print("missing image")
    else:
        cv.imshow("windowM", src)
        src = cv.cvtColor(src, cv.COLOR_RGB2GRAY)
        cv.imshow("windowMy", src)
    return cv.imshow
# TODO: 10) Threshold grayscale (using low numbers)
# TODO: 11) Blur grayscale image
# TODO: 12) Threshold grayscale again (using high numbers)
# TODO: 13) find contours


def findContours(val):
    from __future__ import print_function
    rng.seed(12345)
    threshold = val

    # Detect edges using Canny
    canny_output = cv.Canny(src_gray, threshold, threshold * 2)

    # Find contours
    contours, hierarchy = cv.findContours(
        canny_output, cv.RETR_TREE, cv.CHAIN_APPROX_SIMPLE)

    # Draw contours
    drawing = np.zeros(
        (canny_output.shape[0], canny_output.shape[1], 3), dtype=np.uint8)
    for i in range(len(contours)):
        color = (rng.randint(0, 256), rng.randint(0, 256), rng.randint(0, 256))
        cv.drawContours(drawing, contours, i, color,
                        2, cv.LINE_8, hierarchy, 0)

    # Show in a window
    cv.imshow('Contours', drawing)


    # Load source image
    parser = argparse.ArgumentParser(
        description='Code for Finding contours in your image tutorial.')
    parser.add_argument('--input', help='Path to input image.',
                        default='HappyFish.jpg')
    args = parser.parse_args()

    src = cv.imread("japaneseflowers.jpg", 1)
    if src is None:
        print('Could not open or find the image:', args.input)
        exit(0)

    # Convert image to gray and blur it
    src_gray = cv.cvtColor(src, cv.COLOR_BGR2GRAY)
    src_gray = cv.blur(src_gray, (3, 3))

    # Create Window
    source_window = 'Source'
    cv.namedWindow(source_window)
    cv.imshow(source_window, src)
    max_thresh = 255
    thresh = 100  # initial threshold
    cv.createTrackbar('Canny Thresh:', source_window,
                      thresh, max_thresh, thresh_callback)
    thresh_callback(thresh)

    cv.waitKey()