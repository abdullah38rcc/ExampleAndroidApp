package io.catalyze.android.example;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import io.catalyze.sdk.android.Catalyze;
import io.catalyze.sdk.android.CatalyzeException;
import io.catalyze.sdk.android.CatalyzeListener;
import io.catalyze.sdk.android.CatalyzeUser;
import io.catalyze.sdk.android.FileManager;
import io.catalyze.sdk.android.api.CatalyzeAPIAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Simple screen allowing for uploading and downloading of a file.
 * 
 * @author ault
 */
public class FileManagementActivity extends Activity {

    private int IMAGE_PICKER_SELECT = 1;
    private EditText fileIdEditText;
    private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_management);

        // setup our button listeners
		Button uploadButton = (Button) findViewById(R.id.mainUploadFileButton);
        Button downloadButton = (Button) findViewById(R.id.mainDownloadFileButton);
		fileIdEditText = (EditText) findViewById(R.id.mainFileIdEditText);
        image = (ImageView) findViewById(R.id.mainImageView);

        uploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                // launch the image picker activity looking only for PNGs
                Intent intent = new Intent();
                intent.setType("image/png");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_PICKER_SELECT);
			}
		});

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileIdEditText.length() == 0) {
                    Toast.makeText(FileManagementActivity.this,
                            "You must specify a filesId",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // fetch the file with the ID given in the fileIdEditText field and set it to the
                // image
                FileManager.getFile(fileIdEditText.getText().toString(), new CatalyzeListener<InputStream>() {
                    @Override
                    public void onError(CatalyzeException e) {
                        Toast.makeText(FileManagementActivity.this,
                                "file download failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(InputStream inputStream) {
                        try {
                            image.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                        } catch (Exception e) {
                            Toast.makeText(FileManagementActivity.this,
                                    "image processing failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {
            File file = getFileFromCameraData(data);
            // upload the image giving the SDK the mime type and file
            FileManager.uploadFileToUser("image/png", file, true, new CatalyzeListener<String>() {

                @Override
                public void onError(CatalyzeException e) {
                    Toast.makeText(FileManagementActivity.this,
                            "file upload failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                    Toast.makeText(FileManagementActivity.this,
                            "File uploaded successfully",
                            Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject json = new JSONObject(s);
                        fileIdEditText.setText(json.getString("filesId"));
                    } catch (JSONException je) {
                        Toast.makeText(FileManagementActivity.this,
                                "Could not parse the API response",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * Use for decoding camera response data.
     * @param data
     * @return
     */
    public File getFileFromCameraData(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = {
                MediaStore.Images.Media.DATA
        };
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return new File(picturePath);
    }
}
