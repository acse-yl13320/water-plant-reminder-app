package qrcode;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**

 * Created by Administrator on 2018/11/8 0008.

 * ��ά�롢�����빤����

 */

public class QRtest {

 

    /**

     * CODE_WIDTH����ά���ȣ���λ����

     * CODE_HEIGHT����ά��߶ȣ���λ����

     * FRONT_COLOR����ά��ǰ��ɫ��0x000000 ��ʾ��ɫ

     * BACKGROUND_COLOR����ά�뱳��ɫ��0xFFFFFF ��ʾ��ɫ

     * ��ʾ�� 16 ���Ʊ�ʾ����ǰ��ҳ�� CSS ��ȡɫ��һ���ģ�ע��ǰ����ɫӦ�öԱ����ԣ��糣���ĺڰ�

     */

    private static final int CODE_WIDTH = 500;

    private static final int CODE_HEIGHT = 500;

    private static final int FRONT_COLOR = 0x008080;

    private static final int BACKGROUND_COLOR = 0xFFFFFF;

 

    public static void main(String[] args) {

        String codeContent1 = "ndwoadnwipipwadipwandpowandpnwaidnwaidnwaoidnwioadjklasn"
        		+ "jfjqgoirnqgoineroignqeoirg0qrngi'rejopq";

        createCodeToFile(codeContent1, null, null);

 

    }

 

    /**

     * ���ɶ�ά�� �� ����ΪͼƬ

     */

    /**

     * @param codeContent        :��ά��������ݣ������һ����ҳ��ַ���� https://www.baidu.com/ �� ΢��ɨһɨ��ֱ�ӽ���˵�ַ

     *                           �����һЩ�������� 1541656080837����΢��ɨһɨ��ֱ�ӻ�����Щ����ֵ

     * @param codeImgFileSaveDir :��ά��ͼƬ�����Ŀ¼,�� D:/codes

     * @param fileName           :��ά��ͼƬ�ļ����ƣ�����ʽ,�� 123.png

     */

    public static void createCodeToFile(String codeContent, File codeImgFileSaveDir, String fileName) {

        try {


            if (codeImgFileSaveDir == null || codeImgFileSaveDir.isFile()) {

                codeImgFileSaveDir = FileSystemView.getFileSystemView().getHomeDirectory();

                System.out.println("��ά��ͼƬ����Ŀ¼Ϊ�գ�Ĭ�Ϸ�������...");

            }

            if (!codeImgFileSaveDir.exists()) {

                codeImgFileSaveDir.mkdirs();

                System.out.println("��ά��ͼƬ����Ŀ¼�����ڣ���ʼ����...");

            }

            if (fileName == null || "".equals(fileName)) {

                fileName = new Date().getTime() + ".png";

                System.out.println("��ά��ͼƬ�ļ���Ϊ�գ�������� png ��ʽͼƬ...");

            }

            /**com.google.zxing.EncodeHintType��������ʾ����,ö������

             * EncodeHintType.CHARACTER_SET�������ַ���������

             * EncodeHintType.ERROR_CORRECTION���������У��

             *      ErrorCorrectionLevel�����У���ȼ���L = ~7% correction��M = ~15% correction��Q = ~25% correction��H = ~30% correction

             *      ������ʱ��Ĭ��Ϊ L �ȼ����ȼ���һ�������ɵ�ͼ����ͬ����ɨ��Ľ����һ����

             * EncodeHintType.MARGIN�����ö�ά��߾࣬��λ���أ�ֵԽС����ά���������Խ��

             * */

            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();

            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

            hints.put(EncodeHintType.MARGIN, 1);

 

            /**

             * MultiFormatWriter:���ʽд�룬����һ�������࣬�������������� encode ����������д����������ά��

             *      encode(String contents,BarcodeFormat format,int width, int height,Map<EncodeHintType,?> hints)

             *      contents:������/��ά������

             *      format���������ͣ��� �����룬��ά�� ��

             *      width����Ŀ��

             *      height����ĸ߶�

             *      hints�������ݵı�������

             * BarcodeFormat��ö�ٸó������֪���������ʽ�������������룬�� 1 ά�������룬2 ά�Ķ�ά�� ��

             * BitMatrix��λ(����)������2D����Ҳ������Ҫ�Ķ�ά��

             */

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            BitMatrix bitMatrix = multiFormatWriter.encode(codeContent, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT, hints);

 

            /**java.awt.image.BufferedImage������ͼ�����ݵĿɷ��ʻ���ͼ��ʵ���� RenderedImage �ӿ�

             * BitMatrix �� get(int x, int y) ��ȡ���ؾ������ݣ�ָ��λ����ֵ���򷵻�true����������Ϊǰ��ɫ����������Ϊ����ɫ

             * BufferedImage �� setRGB(int x, int y, int rgb) ��������ͼ������

             *      x������λ�õĺ����꣬����

             *      y������λ�õ������꣬����

             *      rgb�����ص�ֵ������ 16 ����,�� 0xFFFFFF ��ɫ

             */

            BufferedImage bufferedImage = new BufferedImage(CODE_WIDTH, CODE_HEIGHT, BufferedImage.TYPE_INT_BGR);

            for (int x = 0; x < CODE_WIDTH; x++) {

                for (int y = 0; y < CODE_HEIGHT; y++) {
                	if(bitMatrix.get(x, y)) {
                		bufferedImage.setRGB(x, y, FRONT_COLOR);
                	} else {
                		bufferedImage.setRGB(x, y, BACKGROUND_COLOR );
                	}
                    
                    System.out.println(bitMatrix.get(x, y));
                }

            }

 

            /**javax.imageio.ImageIO java ��չ��ͼ��IO

             * write(RenderedImage im,String formatName,File output)

             *      im����д���ͼ��

             *      formatName��ͼ��д��ĸ�ʽ

             *      output��д���ͼ���ļ����ļ�������ʱ���Զ�����

             *

             * ��������Ķ�ά��ͼƬ�ļ�*/

            File codeImgFile = new File(codeImgFileSaveDir, fileName);

            ImageIO.write(bufferedImage, "png", codeImgFile);

 

            System.out.println("��ά��ͼƬ���ɳɹ���" + codeImgFile.getPath());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}

