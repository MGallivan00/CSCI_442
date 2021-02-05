/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class.
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;

class IMP implements MouseListener, ChangeListener {
    JFrame frame;
    JPanel mp;
    JButton start;
    JScrollPane scroll;
    JMenuItem openItem, exitItem, resetItem;
    Toolkit toolkit;
    File pic;
    ImageIcon img;
    int colorX, colorY;
    int[] pixels;
    int[] results;
    //Instance Fields you will be using below

    //This will be your height and width of your 2d array
    int height = 0, width = 0;

    //your 2D array of pixels
    int picture[][];

    /*
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown
     * menu is how you will open an image to manipulate.
     */
    IMP() {
        toolkit = Toolkit.getDefaultToolkit();
        frame = new JFrame("Image Processing Software by Hunter");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu functions = getFunctions();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                quit();
            }
        });
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                handleOpen();
            }
        });
        resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                reset();
            }
        });
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                quit();
            }
        });
        file.add(openItem);
        file.add(resetItem);
        file.add(exitItem);
        bar.add(file);
        bar.add(functions);
        frame.setSize(600, 600);
        mp = new JPanel();
        mp.setBackground(new Color(0, 0, 0));
        scroll = new JScrollPane(mp);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        JPanel butPanel = new JPanel();
        butPanel.setBackground(Color.black);
        start = new JButton("start");
        start.setEnabled(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fun1();
            }
        });
        butPanel.add(start);
        frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(bar);
        frame.setVisible(true);
    }

    /*
     * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods
     * for handling the choice, fun1, fun2, fun3, fun4, etc. etc.
     */

    private JMenu getFunctions() {
        JMenu fun = new JMenu("Functions");

        JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
        JMenuItem rotateItem = new JMenuItem("Rotate 90");
        JMenuItem grayscaleItem = new JMenuItem("Luminosity Grayscale");
        JMenuItem blurItem = new JMenuItem("Blur");
        JMenuItem edgedetectionItem = new JMenuItem("3x3 Edge Detection");
        JMenuItem edgedetection5Item = new JMenuItem("5x5 Edge Detection");
        JMenuItem histogramItem = new JMenuItem("Histogram");
        JMenuItem equalizerItem = new JMenuItem("Equalizer");
        JMenuItem trackerItem = new JMenuItem("Color Tracker");


        firstItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fun1();
            }
        });
        rotateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                rotate90();
            }
        });
        grayscaleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                grayscale();
            }
        });
        blurItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                blur();
            }
        });

        edgedetectionItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt) {
                edgedetection();
            }
        });

        edgedetection5Item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt) {
                edgedetection5();
            }
        });

        histogramItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                histogram();
            }
        });
        equalizerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                equalizer();
            }
        });
        trackerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                tracker();
            }
        });


        fun.add(firstItem);
        fun.add(rotateItem);
        fun.add(grayscaleItem);
        fun.add(blurItem);
        fun.add(edgedetectionItem);
        fun.add(edgedetection5Item);
        fun.add(histogramItem);
        fun.add(equalizerItem);
        fun.add(trackerItem);

        return fun;

    }

    /*
     * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame.
     * You don't need to worry about this method.
     */
    private void handleOpen() {
        img = new ImageIcon();
        JFileChooser chooser = new JFileChooser();
        Preferences pref = Preferences.userNodeForPackage(IMP.class);
        String path = pref.get("DEFAULT_PATH", "");

        chooser.setCurrentDirectory(new File(path));
        int option = chooser.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            pic = chooser.getSelectedFile();
            pref.put("DEFAULT_PATH", pic.getAbsolutePath());
            img = new ImageIcon(pic.getPath());
        }
        width = img.getIconWidth();
        height = img.getIconHeight();

        JLabel label = new JLabel(img);
        label.addMouseListener(this);
        pixels = new int[width * height];

        results = new int[width * height];


        Image image = img.getImage();

        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("Interrupted waiting for pixels");
            return;
        }
        for (int i = 0; i < width * height; i++)
            results[i] = pixels[i];
        turnTwoDimensional();
        mp.removeAll();
        mp.add(label);
        mp.revalidate();
    }

    /*
     * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
     * So this method changes the one dimensional array to a two-dimensional.
     */
    private void turnTwoDimensional() {
        picture = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                picture[i][j] = pixels[i * width + j];
    }

    /*
     *  This method takes the picture back to the original picture
     */
    private void reset() {
        for (int i = 0; i < width * height; i++)
            pixels[i] = results[i];

        turnTwoDimensional();
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);
        mp.revalidate();
    }

    /*
     * This method is called to redraw the screen with the new image.
     */
    private void resetPicture() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pixels[i * width + j] = picture[i][j];
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        mp.setBackground(new Color(0, 0, 0));
        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);
        mp.revalidate();
    }


    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
    private int[] getPixelArray(int pixel) {
        int temp[] = new int[4];
        temp[0] = (pixel >> 24) & 0xff;
        temp[1] = (pixel >> 16) & 0xff;
        temp[2] = (pixel >> 8) & 0xff;
        temp[3] = (pixel) & 0xff;
        return temp;

    }

    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer.
     */
    private int getPixels(int rgb[]) {
        int alpha = 0;
        int rgba = (rgb[0] << 24) | (rgb[1] << 16) | (rgb[2] << 8) | rgb[3];
        return rgba;
    }

    public void getValue() {
        int pix = picture[colorY][colorX];
        int temp[] = getPixelArray(pix);
        System.out.println("Color value " + temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);
    }

    /**************************************************************************************************
     * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is
     * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will
     * have a 2D array called picture that is holding each pixel from your picture.
     *************************************************************************************************/
    /*
     * Example function that just removes all red values from the picture.
     * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array
     * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
     * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B.
     * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
     * integer value so you can give it back to the program and display the new picture.
     */
    private void fun1() {

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                rgbArray[1] = 0;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void rotate90() {

        int[][] temp = new int[width][height]; //temp with proper dimensions

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                temp[j][height-1-i] = picture[i][j]; //moves the pixel into the temp spot
            }
        }

        blackout();
        int h = width; //swap height and width
        width = height;
        height = h;
        picture = new int[height][width]; // resets the length of picture

        picture = temp; // solidifies the temp spots
        resetPicture(); //rewrites the image
    }

    private void blackout(){
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                for(int l = 1; l < 4; l++) {
                    rgbArray[l] = 0;
                }
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();

    }

    private void grayscale() {

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                double gray = .21 * rgbArray[1] + .72* rgbArray[2] + .07*rgbArray[3];

                for(int l = 1; l < 4; l++) {
                    rgbArray[l] = (int)gray;
                }
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void blur() {

        grayscale();

        int[][] temp = new int[height][width]; //temp with proper dimensions

        //goes through all values
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                int rgbArray[] = new int[4];

                //extracts rgb colors
                rgbArray = getPixelArray(picture[i][j]);

                int sum = 0;
                //cycles through a 3x3 mask
                for (int a = -1; a <= 1; a++) {
                    for (int b = -1; b <= 1; b++) {
                        if (((i + a) >= 0 && (j + b) >= 0 && (i + a) < height && (b + j) < width)) {
                            rgbArray = getPixelArray(picture[i + a][j + b]); //grabs the colors for each of the pixels in the mask
                            sum += rgbArray[1]; //sums red
                        }
                    }
                }
                //averages sum from mask
                int gray = (sum /9);

                //puts average back into the array
                rgbArray[1] = gray;
                rgbArray[2] = gray;
                rgbArray[3] = gray;

                //put the new averaged rgb colors into a temp picture
                temp[i][j] = getPixels(rgbArray);;
            }
        }
        picture = temp; // puts temp back into original photo
        resetPicture(); //rewrites the image
    }

    private void edgedetection() {

        grayscale();

        int[][] temp = new int[height][width]; //temp with proper dimensions
        int[][] mask3 = {
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}
        };

        //goes through all values
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                int rgbArray[] = new int[4];
                int[][] neighborhood = new int[3][3]; // get 3-by-3 array of colors in neighborhood

                //cycles through a 3x3 neighborhood
                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        if (((i - 1 + a) >= 0 && (j - 1 + b) >= 0 && (i - 1 + a) < height && (j - 1 + b) < width)) {
                            neighborhood[a][b] = getPixelArray(picture[i - 1 + a][j - 1 + b])[1]; //grabs the color of each pixel in the neighborhood
                        }
                    }
                }
                //averages sum from mask
                // apply filter
                int temp3 = 0;
                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        temp3 += neighborhood[a][b] * mask3[a][b];
                    }
                }

                if (temp3 >= 100) {
                    rgbArray[0] = 255;
                    for (int l = 1; l < 4; l++) {
                        rgbArray[l] = 255;
                    }
                } else {
                    for (int l = 0; l < 4; l++) {
                        rgbArray[l] = 0;
                    }
                }

                //put the new averaged rgb colors into a temp picture
                temp[i][j] = getPixels(rgbArray);
            }
        }
        picture = temp; // puts temp back into original photo
        resetPicture(); //rewrites the image
    }

    private void edgedetection5() {

        grayscale();

        int[][] temp = new int[height][width]; //temp with proper dimensions

        int[][] mask5 = {
                {-1, -1, -1, -1, -1},
                {-1,  0,  0,  0, -1},
                {-1,  0, 16,  0, -1},
                {-1,  0,  0,  0, -1},
                {-1, -1, -1, -1, -1},
        };

        //goes through all values
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                int rgbArray[] = new int[4];
                int[][] neighborhood = new int[5][5]; // get 3-by-3 array of colors in neighborhood

                //cycles through a 5x5 neighborhood
                for (int a = 0; a < 5; a++) {
                    for (int b = 0; b < 5; b++) {
                        if (((i - 2 + a) >= 0 && (j - 2 + b) >= 0 && (i - 2 + a) < height && (j - 2 + b) < width)) {
                            //grabs the color of each pixel in the neighborhood
                            neighborhood[a][b] = getPixelArray(picture[i - 2 + a][j - 2 + b])[1];
                        }
                    }
                }
                //averages sum from mask
                // apply filter
                int temp5 = 0;
                for (int a = 0; a < 5; a++) {
                    for (int b = 0; b < 5; b++) {
                        temp5 += neighborhood[a][b] * mask5[a][b];
                    }
                }

                if (temp5 >= 100) {
                    rgbArray[0] = 255;
                    for (int l = 1; l < 4; l++) {
                        rgbArray[l] = 255;
                    }
                } else {
                    for (int l = 0; l < 4; l++) {
                        rgbArray[l] = 0;
                    }
                }

                //put the new averaged rgb colors into a temp picture
                temp[i][j] = getPixels(rgbArray);
            }
        }
        picture = temp; // puts temp back into original photo
        resetPicture(); //rewrites the image
    }

    private void histogram(){

        //frequency counters for each color & 0-255 value
        int[] redFreq = new int[256];
        int[] greenFreq = new int[256];
        int[] blueFreq = new int[256];

        //Gathering/calculating histogram data (i.e. frequencies)
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++)
            {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                //current pixel RGB values
                int r = rgbArray[1];
                int g = rgbArray[2];
                int b = rgbArray[3];

                //increasing corresponding frequency values by 1.
                redFreq[r]++;
                greenFreq[g]++;
                blueFreq[b]++;

            }
        }

        //adjusting frequencies by dividing by 5 (as suggested by hunter in class)
        for(int i =0; i< 255; i++)
        {
            redFreq[i] = redFreq[i]/5;
            greenFreq[i] = greenFreq[i]/5;
            blueFreq[i] = blueFreq[i]/5;
        }

        JFrame redFrame = new JFrame("Red");
        redFrame.setSize(305, 600);
        redFrame.setLocation(800, 0);
        JFrame greenFrame = new JFrame("Green");
        greenFrame.setSize(305, 600);
        greenFrame.setLocation(1150, 0);
        JFrame blueFrame = new JFrame("blue");
        blueFrame.setSize(305, 600);
        blueFrame.setLocation(1450, 0);

        MyPanel redPanel = new MyPanel(redFreq);
        MyPanel greenPanel = new MyPanel(greenFreq);
        MyPanel bluePanel = new MyPanel(blueFreq);

        redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
        redFrame.setVisible(true);
        greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
        greenFrame.setVisible(true);
        blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
        blueFrame.setVisible(true);
        start.setEnabled(true);
    }

    private void equalizer() {

        //frequency counters for each color & 0-255 value
        int[] redFreq = new int[256];
        int[] greenFreq = new int[256];
        int[] blueFreq = new int[256];
        int[][] temp = new int[height][width];

        int rgbArray[] = new int[4];

        //histogram data
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++)
            {
                rgbArray = getPixelArray(picture[i][j]);

                //increasing corresponding frequency values by 1.
                redFreq[rgbArray[1]]++;
                greenFreq[rgbArray[2]]++;
                blueFreq[rgbArray[3]]++;
            }
        }
        int minred = 1000;
        int mingreen = 1000;
        int minblue = 1000;

        for(int i = 0; i < redFreq.length; i++){
            if(redFreq[i] < minred && redFreq[i] != 0){
                minred = redFreq[i];
            }
            if(greenFreq[i] < mingreen && greenFreq[i] != 0){
                mingreen = greenFreq[i];
            }
            if(blueFreq[i] < minblue && blueFreq[i] != 0){
                minblue = blueFreq[i];
            }
        }

        int equalizer = height*width;
        int cdfred = 0;
        int cdfgreen = 0;
        int cdfblue = 0;

        int red ;
        int green;
        int blue;

        int[] redarr = new int[256];
        int[] greenarr = new int[256];
        int[] bluearr = new int[256];

        for(int i = 0; i < redFreq.length; i++) {
            cdfred += redFreq[i];
            cdfgreen += greenFreq[i];
            cdfblue += blueFreq[i];

            red = 255*(cdfred - minred)/(equalizer - minred);
            green = 255*(cdfgreen - mingreen)/(equalizer - mingreen);
            blue = 255*(cdfblue - minblue)/(equalizer - minblue);

            redarr[i] = Math.round(red);
            bluearr[i] = Math.round(blue);
            greenarr[i] = Math.round(green);
        }

        int rgb[] = new int[4];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++)
            {
                rgb = getPixelArray(picture[i][j]);

                //puts average back into the array
                rgbArray[0] = 255;
                rgbArray[1] = redarr[rgb[1]];
                rgbArray[2] = greenarr[rgb[2]];
                rgbArray[3] = bluearr[rgb[3]];

                //put the new averaged rgb colors into a temp picture
                temp[i][j] = getPixels(rgbArray);;
            }
        }
        picture = temp; // puts temp back into original photo
        resetPicture(); //rewrites the image

    }

    private void tracker() {
        //this came from a lecture
        int rL = 0; int rH = 130;
        int gL = 0; int gH = 200;
        int bL = 180; int bH = 255;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));
        JSlider rHSlider = new JSlider(0, 255);
        JSlider gHSlider = new JSlider(0, 255);
        JSlider bHSlider = new JSlider(0, 255);
        JSlider rLSlider = new JSlider(0, 255);
        JSlider gLSlider = new JSlider(0, 255);
        JSlider bLSlider = new JSlider(0, 255);
        rHSlider.setName("rh");
        rLSlider.setName("rl");
        gHSlider.setName("gh");
        gLSlider.setName("gl");
        bHSlider.setName("bh");
        bLSlider.setName("bl");
        rHSlider.addChangeListener(this);
        rLSlider.addChangeListener(this);
        gHSlider.addChangeListener(this);
        gLSlider.addChangeListener(this);
        bHSlider.addChangeListener(this);
        bLSlider.addChangeListener(this);
        panel.add(rLSlider);
        panel.add(rHSlider);
        panel.add(gLSlider);
        panel.add(gHSlider);
        panel.add(bLSlider);
        panel.add(bHSlider);
        int result = JOptionPane.showConfirmDialog(null, panel, "Tracker", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        System.out.println(result);

        System.out.println(rL + ", " + rH + " :Green: " + gL + ", " + gH + " :Blue: " + bL + ", " + bH);

        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                //if in red threshold
                if (rgbArray[1] >= rL && rgbArray[1] <= rH) {
                    //and in green threshold
                    if (rgbArray[2] >= gL && rgbArray[2] <= gH) {
                        //and in the blue threshold
                        if (rgbArray[3] >= bL && rgbArray[3] <= bH) {
                            //turn matching colors white
                            rgbArray[1] = 255;
                            rgbArray[2] = 255;
                            rgbArray[3] = 255;
                        } else{
                            rgbArray[1] = 0;
                            rgbArray[2] = 0;
                            rgbArray[3] = 0;
                        }
                    }
                }
                rgbArray[0] = 255;
                picture[i][j] = getPixels(rgbArray);
            }
        }
        resetPicture();
    }



    private void quit() {
        System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent m) {
    }

    @Override
    public void mouseExited(MouseEvent m) {
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent m) {
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        JSlider source = (JSlider)ce.getSource();
        if (!source.getValueIsAdjusting())
        {
            if(source.getName().equals("rl")) {
                int rL = source.getValue();
                System.out.println("rL " + rL);
            } else if (source.getName().equals("rh")) {
                int rH = source.getValue();
                System.out.println("rH " + rH);
            } else if (source.getName().equals("gl")) {
                int gL = source.getValue();
                System.out.println("gL " + gL);
            } else if (source.getName().equals("gh")) {
                int gH = source.getValue();
                System.out.println("gH " + gH);
            } else if (source.getName().equals("bl")) {
                int bL = source.getValue();
                System.out.println("bL " + bL);
            } else if (source.getName().equals("bh")) {
                int bH = source.getValue();
                System.out.println("bH " + bH);
            }
        }
    }

    public static void main(String[] args) {
        IMP imp = new IMP();
    }
}

