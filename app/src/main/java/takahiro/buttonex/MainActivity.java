package takahiro.buttonex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private final static String TAG_MESSAGE = "0";
    private final static String TAG_YESNO = "1";
    private final static String TAG_TEXT = "2";
    private final static String TAG_IMAGE = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // レイアウトの作成
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);

        // ボタンの生成
        linearLayout.addView(makeButton("メッセージダイアログの表示", TAG_MESSAGE));
        linearLayout.addView(makeButton("Yes,Noダイアログの表示", TAG_YESNO));
        linearLayout.addView(makeButton("テキスト入力ダイアログの表示", TAG_TEXT));
        linearLayout.addView(makeButton(res2bmp(this, R.drawable.a0120000005), TAG_IMAGE));
    }

    // ボタンの生成
    private Button makeButton(String text, String tag) {
        Button button = new Button(this);
        button.setTag(tag);
        button.setText(text);
        button.setOnClickListener(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
        return button;

    }


    // イメージボタンの生成
    private ImageButton makeButton(Bitmap bitmap, String tag) {
        ImageButton imageButton = new ImageButton(this);
        imageButton.setTag(tag);
        imageButton.setOnClickListener(this);
        imageButton.setImageBitmap(bitmap);
        imageButton.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
        return imageButton;

    }

    // リソース⇒Bitmap
    public Bitmap res2bmp(Context context, int resID) {
        return BitmapFactory.decodeResource(context.getResources(), resID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String tag = (String)v.getTag();

        if(TAG_MESSAGE.equals(tag)) {
            MessageDialog.show(this, "メッセージダイアログ", "ボタンを押した");
        } else if(TAG_YESNO.equals(tag)) {
            YesNoDialog.show(this, "YES/NOダイアログ", "YES/NOを選択");
        } else if(TAG_TEXT.equals(tag)) {
            TextDialog.show(this, "テキスト入力ダイアログ", "テキストを入力");
        } else if(TAG_IMAGE.equals(tag)) {
            MessageDialog.show(this, "", "イメージボタンを押した　");
        }
    }

    // メッセ―ジダイアログの定義
    public static class MessageDialog extends DialogFragment {
        // ダイアログの表示
        public static void show(Activity activity, String title, String text) {
            MessageDialog f = new MessageDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("text", text);
            f.setArguments(args);
            f.show(activity.getFragmentManager(), "MessageDialog");

        }

        // ダイアログの生成
        public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
            ad.setTitle(getArguments().getString("title"));
            ad.setMessage(getArguments().getString("text"));
            ad.setPositiveButton("OK", null);
            return ad.create();

        }
    }

    // Yes/Noダイアログの定義
    public static class YesNoDialog extends DialogFragment {
        // ダイアログの表示
        public static void show(Activity activity, String title, String text) {
            YesNoDialog f = new YesNoDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("text", text);
            f.setArguments(args);
            f.show(activity.getFragmentManager(), "YesNoDialog");

        }

        // ダイアログの生成
        public Dialog onCreateDialog(Bundle bundle) {
            // リスナーの生成
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                // ダイアログボタン押下時に呼ばれる
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == DialogInterface.BUTTON_POSITIVE) {
                        MessageDialog.show(getActivity(), "", "YES");
                    } else if(which == DialogInterface.BUTTON_NEGATIVE) {
                        MessageDialog.show(getActivity(), "", "NO");
                    }
                }
            };

            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
            ad.setTitle(getArguments().getString("title"));
            ad.setMessage(getArguments().getString("text"));
            ad.setPositiveButton("YES", listener);
            ad.setNegativeButton("NO", listener);
            return ad.create();

        }

    }

    // テキスト入力ダイアログの定義
    public static class TextDialog extends DialogFragment {
        private EditText editText;

        // ダイアログの表示
        public static void show(Activity activity, String title, String text) {
            TextDialog f = new TextDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("text", text);
            f.setArguments(args);
            f.show(activity.getFragmentManager(), "TextDialog");

        }

        // テキスト入力ダイアログの生成
        public Dialog onCreateDialog(Bundle bundle) {
            // リスナーの生成
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                // ダイアログボタン押下時に呼ばれる
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MessageDialog.show(getActivity(), "", editText.getText().toString());
                }
            };

            // エディットテキストの生成
            editText = new EditText(getActivity());

            // テキスト入力ダイアログの生成
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
            ad.setTitle(getArguments().getString("title"));
            ad.setMessage(getArguments().getString("text"));
            ad.setView(editText);
            ad.setPositiveButton("OK", listener);

            // フラグメント状態復帰
            if(bundle != null) {
                editText.setText(bundle.getString("editText", ""));
            }

            return ad.create();
        }

        // フラグメントの状態保持
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("editText", editText.getText().toString());
        }
    }


}
