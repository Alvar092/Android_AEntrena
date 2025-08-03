package com.aentrena.db19aentrena.game.heroes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aentrena.db19aentrena.R
import com.aentrena.db19aentrena.game.GameActivity
import com.aentrena.db19aentrena.game.GameViewModel
import com.aentrena.db19aentrena.game.details.FragmentDetails
import com.aentrena.db19aentrena.model.Hero
import com.bumptech.glide.Glide

class HeroAdapter(
    private val viewModel: GameViewModel
): ListAdapter<Hero, HeroAdapter.HeroViewHolder>(HeroDiffCallback()) {
    // Encapsular la vista del item y atarla a los datos
    inner class HeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Referencio a las vistas de item_heroes
        private val nameTv: TextView = itemView.findViewById(R.id.tvHeroName)
        private val photoIv: ImageView = itemView.findViewById(R.id.ivHero)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.pbHeroHP)

        //Vincular un hero a la vista
        fun bind(hero: Hero) {
            nameTv.text = hero.name

            Log.d("HeroAdapter", "Cargando imagen para ${hero.name}: '${hero.photo}'")
            if (hero.photo.isNullOrEmpty()) {
                Log.e("HeroAdapter", "URL de imagen vacía para ${hero.name}")
                photoIv.setImageResource(R.drawable.ball)  // Imagen por defecto
                return
            }

            //val progressBar = itemView.findViewById<ProgressBar>(R.id.pbHeroHP)

            progressBar.progress = hero.currentHealth

            if (!hero.isAlive()) {
                nameTv.setTextColor(android.graphics.Color.RED)
            }

            Glide.with(itemView.context)
                .load(hero.photo)
                .placeholder(R.drawable.ball)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .override(80,80)
                .into(photoIv)

            itemView.setOnClickListener {
                viewModel.selectHero(hero)
            }
        }
    }


    // Inflar el layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hero, parent, false)
        return HeroViewHolder(view)
    }

    //Asociar el dato al viewHolder correspondiente
    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class HeroDiffCallback: DiffUtil.ItemCallback<Hero>() {
    override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        return oldItem == newItem
    }
}
