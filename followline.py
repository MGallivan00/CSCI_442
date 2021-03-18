##Susan McCartney
##Maria Gallivan

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

def getValues(event, x, y, flags, param):
    if event == cv.EVENT_LBUTTONDOWN:  # checks mouse left button down condition
        print("Coordinates of pixel: X: ", x, "Y: ", y)
    return


def nothing(x):
    pass

# capture video from e-puck
timestep = int(robot.getBasicTimeStep())

cam = robot.getDevice("camera")
cam.enable(timestep)

cv.namedWindow("Video")
cv.setMouseCallback("Video", getValues)


cv.namedWindow("Trimmed")
cv.setMouseCallback("Trimmed", getValues)

count = 0
while robot.step(timestep) != -1:
    count = count + 1
    if(count%5 != 0):
        continue
    img = cam.getImage()
    width = cam.getWidth()
    height = cam.getHeight()
    temppic = np.frombuffer(img, np.uint8)
    out = np.reshape(temppic, (height, width, 4))
    out = cv.Canny(out, 200, 300) #edge detection
    cv.imshow("Video", out)

    #x (0:200) y (0:80)
    #Trimmed output of the edge detection
    npImg = np.asarray(out[180:260, 30:230])
    cv.imshow("Trimmed", npImg)

    #array of location of white pixels
    loc_white = np.argwhere(npImg == 255)
    num_white = len(loc_white)
    #print(loc_white[num_white-1][1], ",", loc_white[num_white-1][0])

    val_x = 0
    val_y = 0

    #find center of gravity
    for pix in range(num_white):
        ##        while loc_white[pix-1][1] > 140 and loc_white[pix-1][1] < 120:
        ##            print('Not Centered')
        ##            leftMotor.setVelocity(4.0)
        ##            rightMotor.setVelocity(0.0)
        #add x values
        val_x = val_x + loc_white[pix-1][1]
        val_y = val_y + loc_white[pix-1][0]

    if val_y == 0 or val_x == 0:
        print('Nooooooo')
        cog_y = 0
        cog_x = 0
    else:
        cog_x = val_x/num_white
        cog_y = val_y/num_white

        print(cog_x, ",", cog_y)

        #set conditions based off resulting (x,y)
    if cog_y == 0 and cog_x == 0:
        print('No Line')
        if(count > 1700):
            leftMotor.setVelocity(0.0)
            rightMotor.setVelocity(0.0)
        else:
            leftMotor.setVelocity(0.0)
            rightMotor.setVelocity(1.0)
    elif cog_x < 40:
        print('Turn left')
        leftMotor.setVelocity(-1.75)
        rightMotor.setVelocity(1.25)
    elif cog_x > 175:
        print('Turn right')
        leftMotor.setVelocity(1.25)
        rightMotor.setVelocity(-1.75)
    else:
        print("Straigth Line")
        leftMotor.setVelocity(3.5)
        rightMotor.setVelocity(3.5)


    k = cv.waitKey(1)
    if k == 27:
        break

cv.destroyAllWindows()