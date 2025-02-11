package com.nilam.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.modeldata.modelpegawai

class adapter_data_pegawai (
    private val listPegawai: ArrayList<modelpegawai>) :
    RecyclerView.Adapter<adapter_data_pegawai.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_data_pegawai.ViewHolder, position: Int) {

        val item= listPegawai[position]
        holder.idpegawai.text = item.id_pegawai ?: ""
        holder.tvnamapegawai.text = item.tv_namapegawai ?: ""
        holder.tvalamatpegawai.text = item.tv_alamatpegawai ?: ""
        holder.tvnohppegawai.text = item.tv_nohppegawai ?: ""
        holder.tvterdaftarpegawai.text = item.tv_terdaftarpegawai ?: ""
        holder.tvcabangpegawai.text = item.tv_cabangpegawai ?: ""
        holder.bthubungipegawai.setOnClickListener{

        }
        holder.btlihatpegawai.setOnClickListener{

        }
    }
    override fun getItemCount(): Int {
        return listPegawai.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idpegawai: TextView = itemView.findViewById(R.id.id_pegawai)
        val tvnamapegawai: TextView = itemView.findViewById(R.id.tv_namapegawai)
        val tvalamatpegawai: TextView = itemView.findViewById(R.id.tv_alamatpegawai)
        val tvnohppegawai: TextView = itemView.findViewById(R.id.tv_nohppegawai)
        val tvterdaftarpegawai: TextView = itemView.findViewById(R.id.tv_terdaftarpegawai)
        val tvcabangpegawai: TextView = itemView.findViewById(R.id.tv_cabangpegawai)
        val bthubungipegawai: Button = itemView.findViewById(R.id.bt_hubungipegawai)
        val btlihatpegawai: Button = itemView.findViewById(R.id.bt_lihatpegawai)
    }

}