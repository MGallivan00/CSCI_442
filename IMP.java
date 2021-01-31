/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;

class IMP implements MouseListener {
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
        JMenuItem edgedetectionItem = new JMenuItem("Edge Detection");
        JMenuItem histogramItem = new JMenuItem("Histogram");


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

        histogramItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                histogram();
            }
        });


        fun.add(firstItem);
        fun.add(rotateItem);
        fun.add(grayscaleItem);
        fun.add(blurItem);
        fun.add(edgedetectionItem);
        fun.add(histogramItem);

        return fun;

    }

    /*
     * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame.
     * You don't need to worry about this method.
     */
    private void handleOpen() {
        img = new ImageIcon();
        JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home"), "Documents\\Montana State University Academics\\21 _Spring\\CSCI 442"));
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


                rgbArray[1] = 228;
                rgbArray[2] = 172;
                rgbArray[3] = 52;
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

        int h = width; //swap height and width
        width = height;
        height = h;
        picture = new int[height][width]; // resets the length of picture

        picture = temp; // solidifies the temp spots
        resetPicture(); //rewrites the image
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

                int sumr = 0;
                int sumg = 0;
                int sumb = 0;
                //cycles through a 3x3 mask
                for (int a = -1; a <= 1; a++) {
                    for (int b = -1; b <= 1; b++) {
                        if (((i + a) >= 0 && (j + b) >= 0 && (i + a) < height && (b + j) < width)) {
                            rgbArray = getPixelArray(picture[i + a][j + b]); //grabs the colors for each of the pixels in the mask
                            sumr += rgbArray[1]; //sums red
                            sumg += rgbArray[2]; //sums green
                            sumb += rgbArray[3]; //sums blue
                        }
                    }
                }
                //averages sum from mask
                int red = (sumr /9);
                int green = (sumg /9);
                int blue = (sumb /9);

                //puts average back into the array
                rgbArray[1] = red;
                rgbArray[2] = green;
                rgbArray[3] = blue;

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

        int[][] mask5 = {
                {-1, -1, -1, -1, -1},
                {-1, 0, 0, 0, -1},
                {-1, 0, 16, 0, -1},
                {-1, 0, 0, 0, -1},
                {-1, -1, -1, -1, -1},
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
                int temp3 = 0, temp5 = 0;
                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        temp3 += neighborhood[a][b] * mask3[a][b];
                        //temp5 += neighborhood[a][b] * mask5[a][b];
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



    private void histogram(){
        JFrame redFrame = new JFrame("Red");
        redFrame.setSize(305, 600);
        redFrame.setLocation(800, 0);
        JFrame greenFrame = new JFrame("Green");
        greenFrame.setSize(305, 600);
        greenFrame.setLocation(1150, 0);
        JFrame blueFrame = new JFrame("blue");
        blueFrame.setSize(305, 600);
        blueFrame.setLocation(1450, 0);
        MyPanel redPanel = new MyPanel();
        MyPanel greenPanel = new MyPanel();
        MyPanel bluePanel = new MyPanel();
        redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
        redFrame.setVisible(true);
        greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
        greenFrame.setVisible(true);
        blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
        blueFrame.setVisible(true);
        start.setEnabled(true);
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

    public static void main(String[] args) {
        IMP imp = new IMP();
    }
}

