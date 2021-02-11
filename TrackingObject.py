import cv2 as cv, time
import numpy as np

global hh
global hv
global hs
global lh
global lv
global ls
hh = hv = hs = lh = lv = ls = 10

def getValues(event, x, y, flags, param):
    if event == cv.EVENT_LBUTTONDOWN: #checks mouse left button down condition
        colorsH = hsv[y,x,0]
        colorsS = hsv[y,x,1]
        colorsV = hsv[y,x,2]
        colors = hsv[y,x]
        print("Hue: ",colorsH)
        print("Saturation: ",colorsS)
        print("Lightness: ",colorsV)
        print("BRG Format: ",colors)
        print("Coordinates of pixel: X: ",x,"Y: ",y)
    return

def nothing(x):
    pass

# TODO: 1) Capture live video from Webcam
# https://docs.opencv.org/master/df/d9d/tutorial_py_colorspaces.html
cap = cv.VideoCapture(0)
cv.namedWindow("Video")

cv.namedWindow("Trackbars")
cv.createTrackbar("L - H", "Trackbars", 0, 179, nothing)
cv.createTrackbar("L - S", "Trackbars", 0, 255, nothing)
cv.createTrackbar("L - V", "Trackbars", 0, 255, nothing)
cv.createTrackbar("U - H", "Trackbars", 179, 179, nothing)
cv.createTrackbar("U - S", "Trackbars", 255, 255, nothing)
cv.createTrackbar("U - V", "Trackbars", 255, 255, nothing)

# TODO: 2) Display live video
while(True):
    # Take each frame
    status, src = cap.read()
    # TODO: 3) Convert BGR to HSV
    hsv = cv.cvtColor(src, cv.COLOR_BGR2HSV)
    # TODO: 4) Display HSV video
    cv.imshow("Video", hsv)
    # TODO: 5) Click on HSV video and capture the HSV values at the location clicked, and a few other local values of the item you are going to track.
    # MouseCallback
    cv.setMouseCallback("Video", getValues)
    # define range of blue color in HSV
    # TODO: 6) Using Sliders create scalers for the min and max values you want to track

    lh = cv.getTrackbarPos("L - H", "Trackbars")
    ls = cv.getTrackbarPos("L - S", "Trackbars")
    lv = cv.getTrackbarPos("L - V", "Trackbars")
    hh = cv.getTrackbarPos("U - H", "Trackbars")
    hs = cv.getTrackbarPos("U - S", "Trackbars")
    hv = cv.getTrackbarPos("U - V", "Trackbars")

    min = np.array([lh, ls, lv])
    max = np.array([hh, hs, hv])
    # TODO: 7) Us the OpenCV inRange method to find the values between the scalars from HSV image and the result will go to a grayscale image (make it a binary image, white/black).
    # Threshold the HSV image to get only blue colors
    mask = cv.inRange(hsv, min, max)

    # TODO: 8) Dilate, erode the grayscale image to get a better representation of the object you are tracking.
    # Bitwise-AND mask and original image
    res = cv.bitwise_and(src, src, mask=mask)

    # TODO: Display the original image and the binary image where everything is black except for the object you are tracking. The tracked object will be white.
    mask_3 = cv.cvtColor(mask, cv.COLOR_GRAY2BGR)
    stacked = np.hstack((mask_3, src, res))
    cv.imshow("Original", src)
    cv.imshow("Mask", mask)
    cv.imshow("Res", res)

    if cv.waitKey(1) & 0xFF == ord("q"):
        cv.destroyAllWindows()
        break

cap.release()
cv.destroyAllWindows()



