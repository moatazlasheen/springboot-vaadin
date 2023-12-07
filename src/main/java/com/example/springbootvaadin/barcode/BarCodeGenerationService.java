package com.example.springbootvaadin.barcode;

import java.io.IOException;

public interface BarCodeGenerationService {

	byte[] generateBarCode(String string) throws IOException;

}
