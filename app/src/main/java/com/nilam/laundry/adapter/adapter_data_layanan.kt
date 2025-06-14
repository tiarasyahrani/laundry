package com.nilam.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.modeldata.modellayanan
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class adapter_data_layanan(
    private val listLayanan: ArrayList<modellayanan>
) : RecyclerView.Adapter<adapter_data_layanan.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layanan_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLayanan[position]
        holder.id_layanan.text = item.id_layanan ?: ""
        holder.tvnamalayanan.text = item.tv_namalayanan ?: ""
        holder.tvharga.text = formatRupiah(item.tv_harga)
        holder.tv_layanancabang.text = item.tv_layanancabang ?: ""
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id_layanan: TextView = itemView.findViewById(R.id.id_layanan)
        val tvnamalayanan: TextView = itemView.findViewById(R.id.tv_nama_layanan)
        val tvharga: TextView = itemView.findViewById(R.id.tv_harga_layanan)
        val tv_layanancabang: TextView = itemView.findViewById(R.id.tv_layanancabang)
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
