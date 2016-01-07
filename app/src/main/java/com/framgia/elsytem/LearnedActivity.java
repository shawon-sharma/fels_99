package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.framgia.elsytem.adapters.WordAdapter;
import com.framgia.elsytem.jsonResponse.CategoryResponse;
import com.framgia.elsytem.jsonResponse.WordResponse;
import com.framgia.elsytem.utils.Constants;
import com.framgia.elsytem.utils.Libs;
import com.framgia.elsytem.utils.SessionManager;
import com.framgia.elsytem.utils.Url;
import com.framgia.elsytem.utils.WordReturnByCategory;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LearnedActivity extends AppCompatActivity {
    private ArrayList<String> categoryAll = new ArrayList<>();
    private ArrayList<WordReturnByCategory> item = new ArrayList<>();
    private ArrayList<WordResponse.WordsEntity.AnswersEntity> wordAns;
    private ArrayList<WordResponse.WordsEntity> wordName = new ArrayList<>();
    private ArrayList<CategoriesReturnFromPages> catitem = new ArrayList<>();
    private ArrayList<CategoryResponse.CategoriesEntity> categoriesName = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learned);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Constants.categoryClick = new LinkedHashMap<>();
        Constants.spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        Constants.spinnerOption = (Spinner) findViewById(R.id.spinnerOption);
        Constants.sessionManager = new SessionManager(this);
        Constants.user = Constants.sessionManager.getUserDetails();
        Constants.token = Constants.user.get(Constants.KEY_AUTH_TOKEN);
        Constants.list = (ListView) findViewById(R.id.learned);
        Constants.okHttpClient = new OkHttpClient();
        Constants.url = Url.wordFetchURL;
        Constants.curl = Url.categoryFetchURL;
        try {
            String catnewURL = catURLBody(Constants.categoryPageNumber);
            new HttpAsyncCategory().execute(catnewURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Constants.mIsSpinnerOption = true;
        Constants.spinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!Constants.mIsSpinnerOption) {
                    item.clear();
                    Constants.option = Constants.spinnerOption.getSelectedItem().toString();
                    if (Constants.option.equalsIgnoreCase(Constants.OPTION_ALL)) {
                        Constants.option = Constants.all;
                    } else if (Constants.option.equalsIgnoreCase(Constants.OPTION_LEARN)) {
                        Constants.option = Constants.learned;
                    } else if (Constants.option.equalsIgnoreCase(Constants.OPTION_NOT_LEARN)) {
                        Constants.option = Constants.NOT_LEARNED;
                    }
                    String text = Constants.spinnerCategory.getSelectedItem().toString();
                    String value = Constants.categoryClick.get(text);
                    Constants.category_id = value;
                    Constants.url = Url.wordFetchURL;
                    try {
                        Constants.categoryAdapter.notifyDataSetChanged();
                        String newURL = wordURLBody(Constants.page, Constants.category_id,
                                Constants.option);
                        new HttpAsyncWord().execute(newURL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Constants.mIsSpinnerOption = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Constants.mIsSpinnerFirstCall = true;
        Constants.dataAdapter = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_item, categoryAll);
        Constants.dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Constants.spinnerCategory.setAdapter(Constants.dataAdapter);
        Constants.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!Constants.mIsSpinnerFirstCall) {
                    item.clear();
                    String text = Constants.spinnerCategory.getSelectedItem().toString();
                    String value = Constants.categoryClick.get(text);
                    Constants.category_id = value;

                    Constants.url = Url.wordFetchURL;
                    try {
                        String newURL = wordURLBody(Constants.page, Constants.category_id,
                                Constants.option);
                        new HttpAsyncWord().execute(newURL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Constants.n++;
                } else {
                    Constants.mIsSpinnerFirstCall = false;
                    Constants.n++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Display mDisplay = getWindowManager().getDefaultDisplay();
        Constants.mWidth = mDisplay.getWidth();
        Constants.mWidth /= 2;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_pdf:
                try {
                    createpdf();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(Constants.myFile), getString(R.string.pdf_type));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Intent intent1 = Intent.createChooser(intent, getString(R.string.pdf_open));
                    startActivity(intent1);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_learned, menu);
        return true;
    }

    public void createpdf() throws IOException, DocumentException {
        if (!Libs.isExternalStorageAvailable() || Libs.isExternalStorageReadOnly()) {
        } else {
            File pdfFolder;
            File sdCard = Environment.getExternalStorageDirectory();
            pdfFolder = new File(sdCard.getAbsolutePath() + "/" + Constants.filepath + "/");
            File newFolder = new File(Environment.getExternalStorageDirectory(), Constants.TEST_FOLDER);
            if (!newFolder.exists()) {
                boolean t = newFolder.mkdir();
            }
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
            }
            Constants.myFile = new File(newFolder, Constants.word_list + "_" + System
                    .currentTimeMillis
                            () +
                    Constants.DOT_PDF);
            if (!Constants.myFile.exists())
                Constants.myFile.createNewFile();
            OutputStream output = new FileOutputStream(Constants.myFile);
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, output);
            document.open();
            Font black = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            String text = "";
            for (int i = 0; i < item.size(); i++) {
                text = text + item.get(i).getSingleWord() + "   |   " + getString(R.string.language) + "\n---------" +
                        "---------------------------------------\n";
            }
            Chunk greenText = new Chunk(text, black);
            Paragraph paragraph = new Paragraph(greenText);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.close();
        }
    }

    public String catURLBody(String page) throws Exception {
        LinkedHashMap<String, String> catpara = new LinkedHashMap<>();
        catpara.put(Constants.AUTH_TOKEN, Constants.token);
        catpara.put(Constants.PAGE, page);
        Constants.categoryNewUrl = Libs.makeUrlWithParams(Constants.curl, catpara);
        return Constants.categoryNewUrl;
    }

    public String wordURLBody(String page, String category_id, String option) throws Exception {
        LinkedHashMap<String, String> para = new LinkedHashMap<>();
        para.put(Constants.CCATEGORY_ID, category_id);
        para.put(Constants.OPTION, option);
        para.put(Constants.PAGE, page);
        para.put(Constants.AUTH_TOKEN, Constants.token);
        Constants.newURL = Libs.makeUrlWithParams(Constants.url, para);
        return Constants.newURL;
    }

    private class HttpAsyncCategory extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                Constants.categoryResponse = Constants.okHttpClient.newCall(request).execute();
                Constants.categoryResponseData = Constants.categoryResponse.body().string();
                Constants.gson = new Gson();
                CategoryResponse ca = Constants.gson.fromJson(Constants.categoryResponseData,
                        CategoryResponse.class);
                Constants.cat_total_page = ca.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Constants.cat_total_page;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Integer i) {
            while (i >= 1) {
                String catrl = null;
                try {
                    catrl = catURLBody(String.valueOf(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new HttpAsyncCategoryShow().execute(catrl);
                i--;
            }
        }
    }

    private class HttpAsyncCategoryShow extends AsyncTask<String, Void, ArrayList<CategoryResponse
            .CategoriesEntity>> {
        @Override
        protected ArrayList<CategoryResponse.CategoriesEntity> doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                Constants.categoryResponse = Constants.okHttpClient.newCall(request).execute();
                Constants.categoryResponseData = Constants.categoryResponse.body().string();
                Constants.gson = new Gson();
                CategoryResponse ca = Constants.gson.fromJson(Constants.categoryResponseData, CategoryResponse.class);
                categoriesName = (ArrayList<CategoryResponse.CategoriesEntity>) ca
                        .getCategories();
                Constants.cat_total_page = ca.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return categoriesName;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ArrayList<CategoryResponse.CategoriesEntity> categoriesName) {
            for (int i = 0; i < categoriesName.size(); i++) {
                String key = categoriesName.get(i).getName();
                Integer temporaryValue = categoriesName.get(i).getId();
                String value = String.valueOf(temporaryValue);
                catitem.add(new CategoriesReturnFromPages(key, temporaryValue));
                categoryAll.add(key);
                Constants.categoryClick.put(key, value);
            }
            Constants.spinnerCategory.setAdapter(Constants.dataAdapter);
            Constants.dataAdapter.notifyDataSetChanged();
        }
    }

    private class HttpAsyncWord extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                Constants.request = new Request.Builder().url(urls[0]).get().build();
                Constants.response = Constants.okHttpClient.newCall(Constants.request).execute();
                Constants.responseData = Constants.response.body().string();
                Constants.gson = new Gson();
                Constants.wordResponse = Constants.gson.fromJson(Constants.responseData, WordResponse.class);
                Constants.totalPage = Constants.wordResponse.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Constants.totalPage;
        }

        @Override
        protected void onPreExecute() {
            Constants.mDialog = new ProgressDialog(LearnedActivity.this);
            Constants.mDialog.setTitle(getString(R.string.contacting_servers));
            Constants.mDialog.setMessage(getString(R.string.please_wait));
            Constants.mDialog.setIndeterminate(false);
            Constants.mDialog.setCancelable(true);
            Constants.mDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            while (i >= 1) {
                String newURL = null;
                try {
                    newURL = wordURLBody(String.valueOf(i), Constants.category_id, Constants
                            .option);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new HttpAsyncWordShow().execute(newURL);
                i--;
            }
            Constants.mDialog.dismiss();
        }
    }

    private class HttpAsyncWordShow extends AsyncTask<String, Void, ArrayList<WordResponse
            .WordsEntity>> {
        @Override
        protected ArrayList<WordResponse
                .WordsEntity> doInBackground(String... urls) {
            try {
                Constants.request = new Request.Builder().url(urls[0]).get().build();
                Constants.response = Constants.okHttpClient.newCall(Constants.request).execute();
                Constants.responseData = Constants.response.body().string();
                Constants.gson = new Gson();
                Constants.wordResponse = Constants.gson.fromJson(Constants.responseData,
                        WordResponse
                                .class);
                wordName = (ArrayList<WordResponse.WordsEntity>) Constants.wordResponse.getWords();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return wordName;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(ArrayList<WordResponse.WordsEntity> newWordName) {
            for (int i = 0; i < newWordName.size(); i++) {
                String key = newWordName.get(i).getContent();
                wordAns = (ArrayList<WordResponse
                        .WordsEntity.AnswersEntity>) wordName.get(i).getAnswers();
                item.add(new WordReturnByCategory(key, getString(R.string.language)));
            }
            Constants.categoryAdapter = new WordAdapter(getApplication(), item, Constants.mWidth);
            Constants.list.setAdapter(Constants.categoryAdapter);
            Constants.categoryAdapter.notifyDataSetChanged();
        }
    }
}