package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.BoxWithLabel;
import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.ImageHelperActivity;

public class FaceDetectionActivity extends ImageHelperActivity {
    private FaceDetector faceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                    .enableTracking()
                    .build();
            faceDetector = FaceDetection.getClient(options);
        } catch (Exception ex) {
            Log.e(Constant.TAG, "onCreate: ", ex);
        }
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        InputImage inputImage = InputImage.fromBitmap(outputBitmap, 0);
        faceDetector.process(inputImage).addOnSuccessListener(faces -> {
                    if (faces.isEmpty()) {
                        getMtV().setText(R.string.no_face_detected);
                    } else {
                        List<BoxWithLabel> boxesList = new ArrayList<>();
                        for (Face face : faces) {
                            BoxWithLabel boxWithLabel = new BoxWithLabel(
                                    face.getBoundingBox(),
                                    face.getTrackingId() + " "
                            );
                            boxesList.add(boxWithLabel);
                        }
                        drawDetectionResult(boxesList, outputBitmap);
                        getMtV().setText(MessageFormat.format("{0} has been detected. ", boxesList.size()));
                    }
                })
                .addOnFailureListener(e -> Log.e(Constant.TAG, "onFailure: ", e));
    }
}