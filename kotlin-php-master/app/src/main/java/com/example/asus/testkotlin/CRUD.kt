package com.example.asus.testkotlin

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_crud.*
import org.apache.http.client.methods.HttpGet
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import android.widget.ArrayAdapter
import android.widget.EditText


class CRUD : AppCompatActivity() {

     var id : String = ""
     var name : String = ""
    var hasil : String = ""
    var jsondata : String = ""
    var data : String = ""
     var jObject = JSONObject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud)
        id =  editText.text.toString()
        name = editText2.text.toString()//textView2.toString() //findViewById<EditText>( R.id.editText2).toString()

        try {
            editText.setText(intent.getStringExtra("ID"))
            editText2.setText(intent.getStringExtra("Name"))
            Toast.makeText(getApplicationContext(),""+intent.getStringExtra("Name"),Toast.LENGTH_SHORT).show()
        }
        catch (ex : Exception){
            Toast.makeText(getApplicationContext(),""+ex,Toast.LENGTH_SHORT).show()
        }

    }

    public fun refreshdata(){

        try {
            HttpAsyncTaskPosts().execute("http://team67app.000webhostapp.com/tampil.php")

        }
        catch (ex :Exception ){
            Toast.makeText(getApplicationContext(),"Failed Parsing Data",Toast.LENGTH_SHORT).show()
        }

    }

    public fun btnSimpan (v : View)
    {
        id =  editText.text.toString()
        name = editText2.text.toString()

        HttpAsyncTaskPost().execute("http://team67app.000webhostapp.com/tambah.php?id=$id&name=$name")
        refreshdata()

       // HttpAsyncTaskPost().execute("http://10.0.2.2/maps/tambah.php?id=$id&name=$name")

    }
    public fun btnHapus (v : View)
    {
        id =  editText.text.toString()
        name = editText2.text.toString()
        HttpAsyncTaskPost().execute("http://team67app.000webhostapp.com/hapus.php?id=$id&name=$name")
    }
    public fun btnUpdate(v : View)
    {
        id =  editText.text.toString()
        name = editText2.text.toString()
        HttpAsyncTaskPost().execute("http://team67app.000webhostapp.com/update.php?id=$id&name=$name")
    }

    public fun btnView(v : View)
    {
        id =  editText.text.toString()
        name = editText2.text.toString()
        refreshdata()

        try{
        data = ""
        jsondata = ""
        HttpAsyncTaskPosts().execute("http://team67app.000webhostapp.com/tampil.php")
        Thread.sleep(1000)
        // Show all data
        parseBoundary()
        showMessage("Data", data)}
        catch (ex :Exception){
            Toast.makeText(getBaseContext(), "failed get data", Toast.LENGTH_LONG).show()
        }

3    }

    fun showMessage(title: String, Message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }

    fun POST(url: String, id: String, name : String): String {
        var inputStream: InputStream? = null
        var result = ""
        try {

            // 1. create HttpClient
            val httpclient = DefaultHttpClient()

            // 2. make POST request to the given URL
            val httpGet = HttpGet(url)

            var json = ""

            // 3. build jsonObject
            val jsonObject = JSONObject()
            jsonObject.accumulate("username", id)
            jsonObject.accumulate("name", name)
            //jsonObject.accumulate("latitude", loc.getLatitude())
            // jsonObject.accumulate("updatestatus", loc.getUpdate())


            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString()

            // 5. set json to StringEntity
            val se = StringEntity(json)

            // 6. set httpPost Entity
            //httpGet.sesetEntity(se)

            // 7. Set some headers to inform server about the type of the content
            httpGet.setHeader("Accept", "application/json")
            httpGet.setHeader("Content-type", "application/json")

            // 8. Execute POST request to the given URL
            val httpResponse = httpclient.execute(httpGet)

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent()

            // 10. convert inputstream to string
            if (inputStream != null)
            {
                //result = inputStream.bufferedReader().use(BufferedReader::readText)
                result = convertInputStreamToString(inputStream)
                //Toast.makeText(getBaseContext(), ""+result, Toast.LENGTH_LONG).show()
            }

            else
            {
                result = "Did not work!"
            }

            hasil = result

        } catch (e: Exception) {
            Log.d("InputStream", e.localizedMessage)
        }

        // 11. return result
        return result
    }

    fun GET(url: String, id: String, name : String): String {
        var inputStream: InputStream? = null
        var result = ""
        try {
            jsondata = ""

            // 1. create HttpClient
            val httpclient = DefaultHttpClient()

            // 2. make POST request to the given URL
            val httpGet = HttpGet(url)

            var json = ""

            // 3. build jsonObject
            val jsonObject = JSONObject()
            jsonObject.accumulate("username", id)
            jsonObject.accumulate("name", name)
            //jsonObject.accumulate("latitude", loc.getLatitude())
            // jsonObject.accumulate("updatestatus", loc.getUpdate())


            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString()

            // 5. set json to StringEntity
            val se = StringEntity(json)

            // 6. set httpPost Entity
            //httpGet.sesetEntity(se)

            // 7. Set some headers to inform server about the type of the content
            httpGet.setHeader("Accept", "application/json")
            httpGet.setHeader("Content-type", "application/json")

            // 8. Execute POST request to the given URL
            val httpResponse = httpclient.execute(httpGet)

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent()

            // 10. convert inputstream to string
            if (inputStream != null)
            {
                //result = inputStream.bufferedReader().use(BufferedReader::readText)
                result = convertInputStreamToString(inputStream)
                //Toast.makeText(getBaseContext(), ""+result, Toast.LENGTH_LONG).show()
            }

            else
            {
                result = "Did not work!"
            }


            jsondata += result

        } catch (e: Exception) {
            Log.d("InputStream", e.localizedMessage)
        }

        // 11. return result
        return jsondata
    }

    @Throws(IOException::class)
    private fun convertInputStreamToString(inputStream: InputStream): String {


        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            val result = response.toString()
            return result
        }
    }




    private fun parseBoundary() {
        var xResultBoundary : String =""
        //xResultBoundary = getRequestBoundary("http://10.0.2.2/maps/tampil.php")
        jObject = JSONObject(jsondata)
        val menuitemArray = jObject.getJSONArray("user")
        //var sret = ""
        /*for (i in 0 until menuitemArray.length()) {

            data += menuitemArray.getJSONObject(i).getString("id").toString() + " : "
            data += menuitemArray.getJSONObject(i).getString("name").toString() + "\n"

            var lv: ListView = findViewById<View>(R.id.mainListView) as ListView

            // Instanciating an array list (you don't need to do this,
            // you already have yours).
            val your_array_list = ArrayList<String>()
            your_array_list.add(data)
            //your_array_list.add(name)

            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            val arrayAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list
            )
            lv.setAdapter(arrayAdapter)
        }*/
        //txtResult.setText(sret);
    }



    private inner class HttpAsyncTaskPost : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            return POST(urls[0], id,name)
        }

        override fun onPostExecute(result: String) {
            //Toast.makeText(applicationContext, "User : $userid, Lat : $lat, Long : $lng", Toast.LENGTH_SHORT).show()
            //Toast.makeText(getBaseContext(), ""+hasil, Toast.LENGTH_LONG).show();

        }
    }

    private inner class HttpAsyncTaskPosts : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            return GET(urls[0], id,name)
        }

        override fun onPostExecute(result: String) {
            //Toast.makeText(applicationContext, "User : $userid, Lat : $lat, Long : $lng", Toast.LENGTH_SHORT).show()
            //Toast.makeText(getBaseContext(), ""+hasil, Toast.LENGTH_LONG).show();

        }
    }
}
