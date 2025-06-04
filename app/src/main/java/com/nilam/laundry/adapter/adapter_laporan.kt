package com.nilam.laundry.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.modeldata.model_laporan
import com.google.firebase.database.FirebaseDatabase

class adapter_laporan(
    private val context: Context,
    private val listLaporan: List<model_laporan>,
) : RecyclerView.Adapter<adapter_laporan.LaporanViewHolder>() {

    inner class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvWaktu)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvJenisLayanan: TextView = itemView.findViewById(R.id.tvLayanan)
        val tvLayananTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val btnBayar: Button = itemView.findViewById(R.id.btnAksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val laporan = listLaporan[position]

        holder.tvNama.text = laporan.namaPelanggan
        holder.tvTanggal.text = laporan.tanggal
        holder.tvJenisLayanan.text = laporan.layananUtama

        val tambahanText = if (laporan.tambahan.isNotEmpty())
            laporan.tambahan.joinToString(", ")
        else
            "Tidak ada tambahan"
        holder.tvLayananTambahan.text = tambahanText

        holder.tvTotal.text = laporan.totalBayar

        val isLunas = laporan.status.equals("Lunas", ignoreCase = true)
        holder.tvStatus.text = if (isLunas) "Lunas" else "Belum Dibayar"

        if (isLunas) {
            holder.tvStatus.setBackgroundColor(context.getColor(R.color.green))
            holder.tvStatus.setTextColor(context.getColor(R.color.white))
            holder.btnBayar.visibility = View.GONE
        } else {
            holder.tvStatus.setBackgroundColor(context.getColor(R.color.red))
            holder.tvStatus.setTextColor(context.getColor(R.color.white))
            holder.btnBayar.visibility = View.VISIBLE
        }

        holder.btnBayar.setOnClickListener {
            showMetodePembayaran(context, laporan.idTransaksi, position)
        }
    }

    override fun getItemCount(): Int = listLaporan.size

    private fun showMetodePembayaran(context: Context, transaksiId: String, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_metode_pembayaran_laporan, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).create()

        val onClick: (String) -> Unit = { metode ->
            updateStatusPembayaran(transaksiId, metode, position)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonTunai_laporan).setOnClickListener { onClick("Tunai") }
        dialogView.findViewById<Button>(R.id.buttonQRIS_laporan).setOnClickListener { onClick("QRIS") }
        dialogView.findViewById<Button>(R.id.buttonDANA_laporan).setOnClickListener { onClick("DANA") }
        dialogView.findViewById<Button>(R.id.buttonGoPay_laporan).setOnClickListener { onClick("GoPay") }
        dialogView.findViewById<Button>(R.id.buttonOVO_laporan).setOnClickListener { onClick("OVO") }
        dialogView.findViewById<TextView>(R.id.buttonBatal_laporan).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun updateStatusPembayaran(transaksiId: String, metode: String, position: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("laporan").child(transaksiId)
        val updates = mapOf(
            "metodePembayaran" to metode,
            "status" to "Lunas"
        )

        ref.updateChildren(updates).addOnSuccessListener {
            val laporan = listLaporan[position]
            laporan.metodePembayaran = metode
            laporan.status = "Lunas"

            notifyItemChanged(position)
            Toast.makeText(context, "Pembayaran $metode berhasil", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Gagal memperbarui status pembayaran", Toast.LENGTH_SHORT).show()
        }
    }
}