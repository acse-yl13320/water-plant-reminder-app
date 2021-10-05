package control;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRControl {

    private static final int CODE_WIDTH = 500;
    private static final int CODE_HEIGHT = 500;
    private static final int FRONT_COLOR = 0x008080;
    private static final int BACKGROUND_COLOR = 0xFFFFFF;

    public static void printQR(String payload, String path, String fileName) throws Exception {

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(payload, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT, hints);

        BufferedImage bufferedImage = new BufferedImage(CODE_WIDTH, CODE_HEIGHT, BufferedImage.TYPE_INT_BGR);

        for (int x = 0; x < CODE_WIDTH; x++) {

            for (int y = 0; y < CODE_HEIGHT; y++) {
                if (bitMatrix.get(x, y)) {
                    bufferedImage.setRGB(x, y, FRONT_COLOR);
                } else {
                    bufferedImage.setRGB(x, y, BACKGROUND_COLOR);
                }
            }

        }
        File codeImgFile = new File(path, fileName);

        ImageIO.write(bufferedImage, "png", codeImgFile);

        System.out.println("success! Path:" + codeImgFile.getPath());
    }
}
