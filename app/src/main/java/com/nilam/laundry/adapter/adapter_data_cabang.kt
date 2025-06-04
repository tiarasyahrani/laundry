package com.nilam.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.tambah_cabang
import com.nilam.laundry.modeldata.modelcabang


class adapter_data_cabang (private val listCabang: ArrayList<modelcabang>) :
    RecyclerView.Adapter<adapter_data_cabang.ViewHolder>() {
    lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_cabang, parent,false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= listCabang[position]
        holder.id_cabang.text = item.id_cabang ?: ""
        holder.tv_namacabang.text = item.namacabang ?: ""
        holder.tv_alamatcabang.text = item.alamatcabang ?: ""
        holder.tv_layanancabang.text = item.layanancabang ?: ""

        holder.tv_card_cabang.setOnClickListener {
            val intent = Intent (appContext, tambah_cabang::class.java)
            intent.putExtra("judul", "Edit Cabang")
            intent.putExtra("id", item.id_cabang )
            intent.putExtra("namaCabang", item.namacabang )
            intent.putExtra("alamatCabang", item.alamatcabang )
            intent.putExtra("layananCabang", item.layanancabang )
            appContext.startActivity(intent)



        }
    }
    override fun getItemCount(): Int {
        return listCabang.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_card_cabang: CardView = itemView.findViewById(R.id.tv_card_cabang)
        val id_cabang: TextView = itemView.findViewById(R.id.id_cabang)
        val tv_namacabang: TextView = itemView.findViewById(R.id.tv_nama_cabang)
        val tv_alamatcabang: TextView = itemView.findViewById(R.id.tv_alamat_cabang)
        val tv_layanancabang: TextView = itemView.findViewById(R.id.tv_layanan_cabang)

    }

}