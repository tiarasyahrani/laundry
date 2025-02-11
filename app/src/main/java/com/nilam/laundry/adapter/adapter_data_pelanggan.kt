package com.nilam.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.modeldata.modelpelanggan
import org.w3c.dom.Text

class adapter_data_pelanggan (
    private val listPelanggan: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggann, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: adapter_data_pelanggan.ViewHolder, position: Int) {

        val item = listPelanggan[position]
        holder.id_pelanggan.text = item.id_pelanggan ?: ""
        holder.tv_nama.text = item.tv_nama_pelanggan ?: ""
        holder.tv_nohp.text = item.tv_nohp_pelanggan ?: ""
        holder.tv_terdaftar.text =item.tv_terdaftar_pelanggan ?: ""
        holder.tv_alamat.text = item.tv_alamat_pelanggan ?: ""

        holder.bt_hubungi.setOnClickListener{

        }
        holder.bt_lihat.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id_pelanggan: TextView = itemView.findViewById(R.id.id_pelanggan)
        val tv_nama: TextView = itemView.findViewById(R.id.tv_nama_pelanggan)
        val tv_nohp: TextView = itemView.findViewById(R.id.tv_nohp_pelanggan)
        val tv_alamat: TextView = itemView.findViewById(R.id.tv_alamat_pelanggan)
        val tv_terdaftar: TextView = itemView.findViewById(R.id.tv_terdaftar_pelanggan)
        val bt_hubungi: Button = itemView.findViewById(R.id.bt_hubungipelanggan)
        val bt_lihat: Button = itemView.findViewById(R.id.bt_lihatpelanggan)
    }

}