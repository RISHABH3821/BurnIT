package com.brewingjava.burnit.Activities;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import com.brewingjava.burnit.Helpers.GraphicOverlay;
import com.brewingjava.burnit.R;
import com.brewingjava.burnit.Util.API_PROVIDER;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;

import java.util.concurrent.ExecutionException;

import io.github.erehmi.countdown.CountDownTask;
import io.github.erehmi.countdown.CountDownTimers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.brewingjava.burnit.Constants.StringConstants.squats;
import static java.lang.Math.atan2;

public class WorkoutActivity extends AppCompatActivity {
    private TextView repCounter, calorieCount;
    public static final String POSE_DETECTION = "pose_detection";
    public static final String EXERCISE_TYPE = "exercise_type";
    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    boolean flagCounter = false;
    int counter = 0;
    PoseDetector poseDetector;
    TextView counterText;
    String exerciseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        TextView saveExercise = findViewById(R.id.save_exercise);
        saveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExerciseToDB(counter);
            }
        });
        exerciseType = getIntent().getStringExtra(EXERCISE_TYPE);
        repCounter = findViewById(R.id.rep_count);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        cameraView = findViewById(R.id.camera);
        cameraView.setLifecycleOwner(this);
        counterText = findViewById(R.id.counterText);
        PoseDetectorOptions options =
                new PoseDetectorOptions.Builder()
                        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                        .build();
        poseDetector = PoseDetection.getClient(options);
        cameraView.setFrameProcessingMaxWidth(480);
        cameraView.setFrameProcessingMaxHeight(360);
        CountDownTask countDownTask = CountDownTask.create();
        long targetMillis = CountDownTask.elapsedRealtime() + 1000 * 5;
        final int CD_INTERVAL = 1000;
        countDownTask.until(counterText, targetMillis, CD_INTERVAL, new CountDownTimers.OnCountDownListener() {
            @Override
            public void onTick(View view, long millisUntilFinished) {
                ((TextView) view).setText(String.valueOf(millisUntilFinished / CD_INTERVAL));
            }

            @Override
            public void onFinish(View view) {
                ((TextView) view).setVisibility(View.GONE);
                addFrameProcessor();
            }
        });
    }

    private void saveExerciseToDB(int counter) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Call<String> call = API_PROVIDER.api.saveReps(counter, exerciseType, mAuth.getCurrentUser().getEmail());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(WorkoutActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                WorkoutActivity.this.finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(WorkoutActivity.this, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFrameProcessor() {
        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            @WorkerThread
            public void process(@NonNull Frame frame) {
                Log.d(POSE_DETECTION, "enter frame");
                long time = frame.getTime();
                Size size = frame.getSize();
                int format = frame.getFormat();
                int userRotation = frame.getRotationToUser();
                int viewRotation = frame.getRotationToView();
                graphicOverlay.clear();

                if (frame.getDataClass() == byte[].class) {
                    Log.d(POSE_DETECTION, "using camera api 1");
                    byte[] data = frame.getData();
                    InputImage image = InputImage.fromByteArray(data,
                            /* image width */ size.getWidth(),
                            /* image height */ size.getHeight(),
                            userRotation,
                            format // or IMAGE_FORMAT_YV12
                    );
                    try {
                        Log.d(POSE_DETECTION, "pose detector called");
                        Tasks.await(
                                poseDetector.process(image)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<Pose>() {
                                                    @Override
                                                    public void onSuccess(Pose pose) {
//                                                        message.setText(String.format("%d", pose.getAllPoseLandmarks().size()));
                                                        Log.d(POSE_DETECTION, "success landmarks: " + pose.getAllPoseLandmarks().size());
                                                        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
                                                        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
                                                        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
                                                        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
                                                        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
                                                        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
                                                        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
                                                        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
                                                        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
                                                        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
                                                        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
                                                        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);
                                                        PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
                                                        PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
                                                        PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
                                                        PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
                                                        PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
                                                        PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
                                                        PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
                                                        PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
                                                        PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
                                                        PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);
                                                        PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
                                                        PoseLandmark leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
                                                        PoseLandmark leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
                                                        PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
                                                        PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
                                                        PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
                                                        PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);
                                                        PoseLandmark leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
                                                        PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
                                                        PoseLandmark leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
                                                        PoseLandmark rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);

                                                        if (exerciseType.equals(squats)) {
                                                            if (rightShoulder != null && rightKnee != null && rightHip != null) {
                                                                counterText.setVisibility(View.INVISIBLE);
                                                                double angle = getAngle(rightShoulder, rightHip, rightKnee);
                                                                if (angle < 90) {
                                                                    if (flagCounter) {
                                                                        Toast.makeText(WorkoutActivity.this, "1 rep completed", Toast.LENGTH_SHORT).show();
                                                                        counter++;
                                                                        repCounter.setText(String.format("%d", counter));
                                                                        flagCounter = false;
                                                                    }
                                                                } else if (angle > 120) {
                                                                    flagCounter = true;
                                                                }
                                                            } else {
                                                                counterText.setText("Stand at distance facing right");
                                                                counterText.setVisibility(View.VISIBLE);
                                                            }
                                                        } else {
                                                            //count pushUps.

                                                        }
                                                    }
                                                })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Task failed with an exception
                                                        // ...
                                                        Log.e(POSE_DETECTION, "failure " + e.getMessage());
                                                        e.printStackTrace();
                                                    }
                                                }));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (frame.getDataClass() == Image.class) {
                    Log.d(POSE_DETECTION, "using camera api 2");
                    Image data = frame.getData();
                    // Process android.media.Image...
                    InputImage image =
                            InputImage.fromMediaImage(data, frame.getRotationToUser());
                }
            }
        });
    }


    static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }


}