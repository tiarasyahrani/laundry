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
import com.nilam.laundry.TambahPegawai
import com.nilam.laundry.modeldata.modelpegawai

class adapter_data_pegawai (private val listPegawai: ArrayList<modelpegawai>) :
    RecyclerView.Adapter<adapter_data_pegawai.ViewHolder>() {
    lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent,false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= listPegawai[position]
        holder.id_pegawai.text = item.id_pegawai ?: ""
        holder.tv_namapegawai.text = item.namaPegawai ?: ""
        holder.tv_alamatpegawai.text = item.alamatPegawai ?: ""
        holder.tv_nohppegawai.text = item.noHPPegawai ?: ""
        holder.tv_cabangpegawai.text = item.cabangPegawai ?: ""
        holder.tv_terdaftarpegawai.text = item.terdaftarPegawai ?: ""
        holder.bthubungipegawai.setOnClickListener{
            val builder = AlertDialog.Builder(appContext)


        }
        holder.btlihatpegawai.setOnClickListener{

        }

        holder.tv_card_pegawai.setOnClickListener {
            val intent = Intent (appContext, TambahPegawai::class.java)
            intent.putExtra("judul", "Edit Pegawai")
            intent.putExtra("id", item.id_pegawai )
            intent.putExtra("namaPegawai", item.namaPegawai )
            intent.putExtra("noHPPegawai", item.noHPPegawai )
            intent.putExtra("alamatPegawai", item.alamatPegawai )
            intent.putExtra("idCabang", item. cabangPegawai)
            appContext.startActivity(intent)



        }
    }
    override fun getItemCount(): Int {
        return listPegawai.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_card_pegawai: CardView = itemView.findViewById(R.id.tv_card_pegawai)
        val id_pegawai: TextView = itemView.findViewById(R.id.id_pegawai)
        val tv_namapegawai: TextView = itemView.findViewById(R.id.tv_namapegawai)
        val tv_alamatpegawai: TextView = itemView.findViewById(R.id.tv_alamatpegawai)
        val tv_nohppegawai: TextView = itemView.findViewById(R.id.tv_nohppegawai)
        val tv_terdaftarpegawai: TextView = itemView.findViewById(R.id.tv_terdaftarpegawai)
        val tv_cabangpegawai: TextView = itemView.findViewById(R.id.tv_cabangpegawai)
        val bthubungipegawai: Button = itemView.findViewById(R.id.bt_hubungipegawai)
        val btlihatpegawai: Button = itemView.findViewById(R.id.bt_lihatpegawai)
    }

}