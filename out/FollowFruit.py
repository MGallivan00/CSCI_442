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
        print("Hue: \t", colorsH)
        print("Saturation: \t", colorsS)
        print("Lightness: \t", colorsV)
        print("BRG Format: \t", colors)
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

while robot.step(timestep) != -1:
    img = cam.getImage()
    width = cam.getWidth()
    height = cam.getHeight()
    temppic = np.frombuffer(img, np.uint8)
    out = np.reshape(temppic, (height, width, 4))
    cv.imshow("Video", out)


    #Creates a threshold for the orange colors
    hsv = cv.cvtColor(out, cv.COLOR_BGR2HSV)
    min = np.array([19, 132, 138])
    max = np.array([21, 150, 155])

    mask = cv.inRange(hsv, min, max)

    res = cv.bitwise_and(out, out, mask=mask)

    #calculates number of pixels in the threshold
    n_white_pix = np.sum(res == 255)
    print('Number of white pixels:', n_white_pix)

    #grab contours
    gray_image = cv.cvtColor(res, cv.COLOR_BGR2GRAY)
    ret,thresh = cv.threshold(gray_image,127,255,0)
    M = cv.moments(thresh)

    # calculate x,y coordinate of center
    if M["m00"] != 0: #checks for division by zero
        x = int(M["m10"] / M["m00"])
        y = int(M["m01"] / M["m00"])
        print('Center (', x, ', ', y, ')')
    else:
        x = 140
        y = 0
        print('Center (NA, NA)')

    #Rotates the robot left and right
    if x > 170:
        print('Turn right')
        leftMotor.setVelocity(2.5)
        rightMotor.setVelocity(0.0)
    elif x < 90:
        print('Turn left')
        leftMotor.setVelocity(0.0)
        rightMotor.setVelocity(2.5)

    #Moves the robot forward and backwards
    if n_white_pix > 920:
        print('Move backwards')
        leftMotor.setVelocity(-5.0)
        rightMotor.setVelocity(-5.0)
    elif n_white_pix < 720:
        print('Move forwards')
        leftMotor.setVelocity(5.0)
        rightMotor.setVelocity(5.0)

    k = cv.waitKey(1)
    if k == 27:
        break

cv.destroyAllWindows()

