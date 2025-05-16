package com.nilam.laundry.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.Context
import com.nilam.laundry.R
import com.nilam.laundry.adapter.adapter_data_pelanggan.ViewHolder
import com.nilam.laundry.modeldata.modelpelanggan
import transaksi.transaksi

class adapter_pilih_pelanggan (
    private val PelangganList: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_pilih_pelanggan.ViewHolder>() {
    lateinit var appContext: android.content.Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_pelanggan, parent,false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int) {
        val nomor = position + 1
        val item = PelangganList[position]
        holder.tvID.text = "[$nomor]"
        holder.tvNama.text = item.tv_nama_pelanggan
        holder.tvAlamat.text = "Alamat : ${item.tv_alamat_pelanggan}"
        holder.tvNoHP.text = "No HP : ${item.tv_nohp_pelanggan}"
        holder.cvCARD.setOnClickListener {
            val intent = Intent(appContext, transaksi::class.java)
            intent.putExtra("idPelanggan", item.id_pelanggan)
            intent.putExtra("nama", item.tv_nama_pelanggan)
            intent.putExtra("noHP", item.tv_nohp_pelanggan)

            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()

        }

    }
    override fun getItemCount(): Int {
        return PelangganList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cvCARD: CardView = itemView.findViewById(R.id.card_pilih_pelanggan)
        val tvID: TextView = itemView.findViewById(R.id.no_pelanggan)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_pelanggan)
        val tvAlamat: TextView = itemView.findViewById(R.id.tv_alamat_pelanggan)
        val tvNoHP: TextView = itemView.findViewById(R.id.tv_nohp_pelanggan)

    }


}