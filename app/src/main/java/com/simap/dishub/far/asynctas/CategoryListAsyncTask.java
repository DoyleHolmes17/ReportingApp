package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.Category;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.util.HttpOpenConnection;
import com.simap.dishub.far.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aviroez on 23/04/2015.
 */
public class CategoryListAsyncTask extends AsyncTask<Object, Integer, List<Category>> {

    private Context context;
    private String header;

    public CategoryListAsyncTask(Context context, String header) {
        this.context = context;
        this.header = header;
    }

    @Override
    protected void onPostExecute(List<Category> o) {
    }

    @Override
    protected List<Category> doInBackground(Object... objects) {
        List<Category> listCategory = new ArrayList<Category>();
        JSONObject reader = null;
        String url = Urls.urlmas + Urls.category;
        String result = HttpOpenConnection.get(url, header);

        try {
            if (result != null) {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;
                if (data != null) {
                    JSONArray jsonArray = reader.getJSONArray(FieldName.DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_category = jsonObject.has(FieldName.ID_CATEGORY) ? jsonObject.getString(FieldName.ID_CATEGORY) : "";
                        String category_infra = jsonObject.has(FieldName.CATEGORY_INFRA) ? jsonObject.getString(FieldName.CATEGORY_INFRA) : "";
                        String sub_category_infra = jsonObject.has(FieldName.SUB_CATEGORY_INFRA) ? jsonObject.getString(FieldName.SUB_CATEGORY_INFRA) : "";

                        Category category = new Category();
                        category.setStatus(status);
                        category.setMessage(message);
                        category.setId_category(id_category);
                        category.setCategory_infra(category_infra);
                        category.setSub_category_infra(sub_category_infra);
                        listCategory.add(category);
                    }
                }
            } else {
                Category category = new Category();
                category.setStatus("0");
                category.setMessage("Could not connect to Internet");
                listCategory.add(category);
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return listCategory;
    }
}