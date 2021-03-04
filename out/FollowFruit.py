from controller import *
import cv2 as cv
import numpy as np

robot = Robot()
print("hello")

leftMotor = robot.getDevice('left wheel motor')
rightMotor = robot.getDevice('right wheel motor')
leftMotor.setPosition(float('inf'))
rightMotor.setPosition(float('inf'))
leftMotor.setVelocity(0.0)
rightMotor.setVelocity(0.0)

global hh
global hv
global hs
global lh
global lv
global ls
hh = hv = hs = lh = lv = ls = 10


def getValues(event, x, y, flags, param):
    if event == cv.EVENT_LBUTTONDOWN:  # checks mouse left button down condition
        colorsH = hsv[y, x, 0]
        colorsS = hsv[y, x, 1]
        colorsV = hsv[y, x, 2]
        colors = hsv[y, x]
        print("Hue: ", colorsH)
        print("Saturation: ", colorsS)
        print("Lightness: ", colorsV)
        print("BRG Format: ", colors)
        print("Coordinates of pixel: X: ", x, "Y: ", y)
    return


def nothing(x):
    pass

# TODO: capture video from e-puck
timestep = int(robot.getBasicTimeStep())

cam = robot.getDevice("camera")
cam.enable(timestep)

cv.namedWindow("Video")
cv.setMouseCallback("Video", getValues)

once = True
while robot.step(timestep) != -1:
    leftMotor.setVelocity(2.6)
    rightMotor.setVelocity(2.6)
    img = cam.getImage()
    width = cam.getWidth()
    height = cam.getHeight()
    temppic = np.frombuffer(img, np.uint8)
    out = np.reshape(temppic, (height, width, 4))
    cv.imshow("Video", out)
    pass

if cv.waitKey(1) & 0xFF == ord("q"):
    cv.destroyAllWindows()
