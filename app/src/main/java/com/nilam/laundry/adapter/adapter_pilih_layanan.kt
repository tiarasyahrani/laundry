package com.nilam.laundry.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.modeldata.modellayanan
import transaksi.transaksi
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class adapter_pilih_layanan(
    layananList: List<modellayanan>
) : RecyclerView.Adapter<adapter_pilih_layanan.ViewHolder>() {

    private var originalList: ArrayList<modellayanan> = ArrayList(layananList)
    private var filteredList: ArrayList<modellayanan> = ArrayList(layananList)
    lateinit var appContext: android.content.Context

    fun updateData(newList: List<modellayanan>) {
        originalList.clear()
        originalList.addAll(newList)
        filterList("") // Tampilkan semua saat update data
    }

    fun filterList(keyword: String) {
        filteredList = if (keyword.isBlank()) {
            ArrayList(originalList)
        } else {
            ArrayList(originalList.filter {
                it.tv_namalayanan?.contains(keyword, ignoreCase = true) == true ||
                        it.tv_harga?.contains(keyword, ignoreCase = true) == true
            })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_layanan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = filteredList[position]
        holder.tvID.text = "[$nomor]"
        holder.tv_nama.text = item.tv_namalayanan
        holder.tv_harga.text = formatRupiah(item.tv_harga)

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
        return filteredList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD: CardView = itemView.findViewById(R.id.card_pilih_layanan)
        val tvID: TextView = itemView.findViewById(R.id.no_layanan)
        val tv_nama: TextView = itemView.findViewById(R.id.tv_nama_layanan)
        val tv_harga: TextView = itemView.findViewById(R.id.tv_harga_layanan)
    }

    private fun formatRupiah(nilai: String?): String {
        return try {
            val cleanValue = nilai?.replace(",", ".")?.replace(Regex("[^\\d.]"), "") ?: "0"
            val number = cleanValue.toDouble()
            val symbols = DecimalFormatSymbols().apply {
                groupingSeparator = '.'
                decimalSeparator = ','
            }
            val formatter = DecimalFormat("#,##0.00", symbols)
            "Rp${formatter.format(number)}"
        } catch (e: Exception) {
            "Rp0,00"
        }
    }
}
