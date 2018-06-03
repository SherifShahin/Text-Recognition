package textrecognition.blog.com.textrecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class text_Recognition_image extends AppCompatActivity
{
    private Button get_image;
    private Button process;
    private ImageView imageView;
    private TextView result_txt;
    static final int pick_image=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text__recognition_image);

        get_image=(Button)findViewById(R.id.gallery);
        process=(Button)findViewById(R.id.process);
        imageView=(ImageView)findViewById(R.id.imageView);
        result_txt=(TextView)findViewById(R.id.text_recognition);


        get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"choose image"),pick_image);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == pick_image && resultCode == RESULT_OK)
        {
            Uri image=data.getData();
            Picasso.with(getApplicationContext()).load(image).into(imageView);
            result_txt.setText("");
            try
            {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);

                process.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();

                        if(!textRecognizer.isOperational())
                        {
                            Toast.makeText(getApplicationContext(),"/:",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            Frame frame=new Frame.Builder().setBitmap(bitmap).build();
                            SparseArray<TextBlock> items=textRecognizer.detect(frame);
                            StringBuilder stringBuilder=new StringBuilder();
                            for(int i=0;i<items.size();++i)
                            {
                                TextBlock item=items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            result_txt.setText(stringBuilder.toString());
                        }
                    }
                });



            }

            catch (IOException e)
            {e.printStackTrace();}


        }
    }
}
