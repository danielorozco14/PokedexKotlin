package com.naldana.ejemplo08

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.naldana.ejemplo08.models.Pokemon
import com.naldana.ejemplo08.network.HttpHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: PokemonAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var poke: MutableList<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycler()
        GetPokemons().execute()


    }


    fun initRecycler(){

       /** var pokemon: MutableList<Pokemon> = MutableList(100) {i ->
            Pokemon(i,"Name")
        }**/
        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(poke)
        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private inner class GetPokemons : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg arg0: Void): Void? {
            val sh = HttpHandler()
            val url = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=300"
            val jsonStr = sh.makeServiceCall(url)

            //Log.d(TAG,jsonStr);

            if (jsonStr != null) {
                try {
                    //Log.d(TAG,"ENTRANDO AL TRY");
                    //SE OBTIENE EL JSON COMPLETO
                    val jsonObj = JSONObject(jsonStr)
                    //SE OBTIENE EL NOMBRE DEL ARREGLO
                    val pokemon = jsonObj.getJSONArray("results")

                    for (i in 0 until pokemon.length()) {
                        //SE OBTIENE EL INDICE DEL OBJETO
                        val c = pokemon.getJSONObject(i)
                        //SE OBTIENE EL ATRIBUTO QUE SE QUIERE AGREGAR A LA LISTA
                        val name = c.getString("name").toUpperCase()
                        //Log.d(TAG,name);

                         poke= MutableList(100) {i ->
                            Pokemon(i,name)
                        }


                        //HashMap<String, String> pokeDex = new HashMap<>();

                        // pokeDex.put("name", name);
                        // pokemonList.add(pokeDex);
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                //Log.e(TAG, "No se pudo obtener el JSON");
            }

            return null
        }

        override fun onPostExecute(result: Void) {
            super.onPostExecute(result)
        }
    }

}
