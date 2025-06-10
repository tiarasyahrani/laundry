package com.nilam.laundry.adapter

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.modeldata.model_laporan
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat

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
        val tvDiambil: TextView = itemView.findViewById(R.id.tvDiambil)
        val tvAngka: TextView = itemView.findViewById(R.id.tvAngka)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val laporan = listLaporan[position]

        holder.tvAngka.text = "[${position + 1}]"
        holder.tvNama.text = laporan.namaPelanggan
        holder.tvTanggal.text = laporan.tanggal
        holder.tvJenisLayanan.text = laporan.layananUtama

        val tambahanText = if (laporan.tambahan.isNotEmpty())
            laporan.tambahan.joinToString(", ")
        else
            context.getString(R.string.tidak_ada_tambahan)
        holder.tvLayananTambahan.text = tambahanText

        holder.tvTotal.text = laporan.totalBayar

        val isLunas = laporan.status.equals("Lunas", ignoreCase = true)
        val sudahDiambil = laporan.jamAmbil.isNotEmpty()

        if (!isLunas) {
            holder.tvStatus.text = context.getString(R.string.status_belum_dibayar)
            holder.tvStatus.setBackgroundColor(context.getColor(R.color.red))
            holder.tvStatus.setTextColor(context.getColor(R.color.white))
            holder.btnBayar.visibility = View.VISIBLE
            holder.btnBayar.text = context.getString(R.string.aksi_bayar)
            holder.tvDiambil.visibility = View.GONE
            holder.btnBayar.setBackgroundColor(context.getColor(R.color.red))
            holder.btnBayar.setTextColor(context.getColor(R.color.white))

            holder.btnBayar.setOnClickListener {
                showMetodePembayaran(context, laporan.idTransaksi, position)
            }
        } else {
            holder.tvStatus.text = context.getString(R.string.status_lunas)
            holder.tvStatus.setBackgroundColor(context.getColor(R.color.green))
            holder.tvStatus.setTextColor(context.getColor(R.color.white))

            if (!sudahDiambil) {
                holder.btnBayar.visibility = View.VISIBLE
                holder.btnBayar.text = context.getString(R.string.aksi_diambil)
                holder.tvDiambil.visibility = View.GONE
                holder.btnBayar.setBackgroundColor(context.getColor(R.color.green))
                holder.btnBayar.setTextColor(context.getColor(R.color.white))

                holder.btnBayar.setOnClickListener {
                    showJamAmbilDialog(context, laporan.idTransaksi, position)
                }
            } else {
                holder.btnBayar.visibility = View.GONE
                holder.tvDiambil.visibility = View.VISIBLE
                holder.tvDiambil.text = "Diambil pada ${formatTanggalLengkap(laporan.jamAmbil)}"            }
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

        dialogView.findViewById<Button>(R.id.buttonTunai_laporan).setOnClickListener { onClick(context.getString(R.string.metode_tunai)) }
        dialogView.findViewById<Button>(R.id.buttonQRIS_laporan).setOnClickListener { onClick(context.getString(R.string.metode_qris)) }
        dialogView.findViewById<Button>(R.id.buttonDANA_laporan).setOnClickListener { onClick(context.getString(R.string.metode_dana)) }
        dialogView.findViewById<Button>(R.id.buttonGoPay_laporan).setOnClickListener { onClick(context.getString(R.string.metode_gopay)) }
        dialogView.findViewById<Button>(R.id.buttonOVO_laporan).setOnClickListener { onClick(context.getString(R.string.metode_ovo)) }
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
            Toast.makeText(context, "Pembayaran $metode berhasil", Toast.LENGTH_SHORT).show()        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.gagal_update_pembayaran), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showJamAmbilDialog(context: Context, transaksiId: String, position: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)

            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val tanggalJamAmbil = formatter.format(calendar.time)

            updateJamAmbil(transaksiId, tanggalJamAmbil, position)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun updateJamAmbil(transaksiId: String, jamAmbil: String, position: Int) {
        val ref = FirebaseDatabase.getInstance().   getReference("laporan").child(transaksiId)
        val updates = mapOf(
            "jamAmbil" to jamAmbil
        )

        ref.updateChildren(updates).addOnSuccessListener {
            val laporan = listLaporan[position]
            laporan.jamAmbil = jamAmbil

            notifyItemChanged(position)
            Toast.makeText(context, "Jam ambil disimpan: $jamAmbil", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.gagal_simpan_jam_ambil), Toast.LENGTH_SHORT).show()
        }
    }
}

private fun formatTanggalLengkap(tanggalJam: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(tanggalJam)
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale("id", "ID"))
        outputFormat.format(date!!)
    } catch (e: Exception) {
        tanggalJam
    }
}
