package com.example.springbootvaadin.barcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

@Service
public class BarCodeGenerationServiceImpl implements BarCodeGenerationService{

	@Override
	public byte[] generateBarCode(String string) throws IOException {
		BitMatrix bitMatrix;
	    Map<EncodeHintType, Integer> hints = new HashMap<>();
	    hints.put(EncodeHintType.MARGIN, Integer.valueOf(0));

	    bitMatrix = new Code128Writer().encode(string, BarcodeFormat.CODE_128, string.length() * 11, 20, hints);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
	    return baos.toByteArray();
	}

}
