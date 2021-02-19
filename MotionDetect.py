import cv2 as cv
import numpy as np


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

    # TODO: brighten image a bit
    # frame = frame*bright

    # TODO: 5) blur the image
    gray = cv.GaussianBlur(frame, (21, 21), 0)

    # TODO: 6) take the running average of frame
    cv.accumulateWeighted(gray, imageForAve, .04)

    # TODO: 7) swap running average results from step 6 to same bits as frame:
    avg = cv.convertScaleAbs(imageForAve)

    # TODO: 8) Take difference, built in OpenCV function to do a diff between two images
    diff = cv.absdiff(gray, avg)

    # TODO: 9) Convert to grayscale
    g = cv.cvtColor(diff, cv.COLOR_BGR2GRAY)
    cv.imshow("Grayscale", g)

    # TODO: 10) Threshold grayscale (using low numbers)
    _, bw = cv.threshold(g, 30, 255, cv.THRESH_BINARY)
    cv.imshow("Threshold Low", bw)

    # TODO: 11) Blur grayscale image
    g2 = cv.blur(bw, (21, 21), 0)

    # TODO: 12) Threshold grayscale again (using high numbers)
    _, bw = cv.threshold(g2, 100, 255, cv.THRESH_BINARY)
    cv.imshow("Threshold High", bw)

    # TODO: 13) find contours
    contours, _ = cv.findContours(bw, cv.RETR_CCOMP, cv.CHAIN_APPROX_SIMPLE)
    # cv.drawContours(frame, contours, -1, (0, 255, 0), 3)

    for contour in contours:
        if cv.contourArea(contour) < 100000:
            (x, y, w, h) = cv.boundingRect(contour)
            cv.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 3)

    cv.imshow("Contours", frame)

    if cv.waitKey(1) & 0xFF == ord("q"):
        cv.destroyAllWindows()
        break

cap.release()
