package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Preeti on 11-12-2017.
 */

public class Knowledge_Centre_Detail extends Activity {
    TextView title_side_Tv;

    LinearLayout main_LinearLayout;

    TextView dr_name_TV;

    TextView viewTv;

    TextView titleTv;

    TextView descriptionTv;

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                try {


                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine == 0) {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "view less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "view more", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knwoledge_detail);
        title_side_Tv = (TextView) findViewById(R.id.title_side);
        dr_name_TV = (TextView) findViewById(R.id.dr_name);
        viewTv = (TextView) findViewById(R.id.view);
        titleTv = (TextView) findViewById(R.id.title);
        main_LinearLayout = (LinearLayout) findViewById(R.id.main_lay);
        descriptionTv = (TextView) findViewById(R.id.description);
        if (descriptionTv.getText().length() > 170)
            makeTextViewResizable(descriptionTv, 3, "view more", true);
        else {
            // descriptionTv.setText(description);
        }
        descriptionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Knowledge_Centre_Detail.this,Module_Detail_Activity.class);
                startActivity(intent);
            }
        });
        TypeFaceMethods.setRegularTypeFaceForTextView(title_side_Tv, this);
        TypeFaceMethods.setRegularTypeFaceForTextView(dr_name_TV, this);
        TypeFaceMethods.setRegularTypeFaceForTextView(viewTv, this);
        TypeFaceMethods.setRegularTypeFaceForTextView(titleTv, this);
        TypeFaceMethods.setRegularTypeFaceForTextView(descriptionTv, this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 3; i++) {
            try {
                View v = LayoutInflater.from(this).inflate(R.layout.module_item, null);
                LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.lay);

                for (int j = 0; j < 3; j++) {
                    View v1 = LayoutInflater.from(this).inflate(R.layout.module_item_check, null);

                    linearLayout.addView(v1);
                }
                main_LinearLayout.addView(v);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
