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
import com.nilam.laundry.adapter.adapter_data_layanan.ViewHolder
import com.nilam.laundry.modeldata.modellayanan
import com.nilam.laundry.modeldata.modelpelanggan
import transaksi.transaksi

class adapter_pilih_layanan (
    private val LayananList: ArrayList<modellayanan>) :
    RecyclerView.Adapter<adapter_pilih_layanan.ViewHolder>() {
    lateinit var appContext: android.content.Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_layanan, parent,false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int) {
        val nomor = position + 1
        val item = LayananList[position]
        holder.tvID.text = "[$nomor]"
        holder.tv_nama.text = item.tv_namalayanan
        holder.tv_harga.text = item.tv_harga
        holder.cvCARD.setOnClickListener {
            val intent = Intent(appContext, transaksi::class.java)
            intent.putExtra("id", item.id_layanan)
            intent.putExtra("nama", item.tv_namalayanan)
            intent.putExtra("harga", item.tv_harga)

            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()

        }

    }
    override fun getItemCount(): Int {
        return LayananList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cvCARD: CardView = itemView.findViewById(R.id.card_pilih_layanan)
        val tvID: TextView = itemView.findViewById(R.id.no_layanan)
        val tv_nama: TextView = itemView.findViewById(R.id.tv_nama_layanan)
        val tv_harga: TextView = itemView.findViewById(R.id.tv_harga_layanan)

    }


}