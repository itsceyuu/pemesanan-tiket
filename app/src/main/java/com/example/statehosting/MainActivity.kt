package com.example.statehosting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.statehosting.ui.theme.StateHostingTheme
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            StateHostingTheme {
                TicketPage()
                }
            }
        }
    }

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    number: Int,
    label: String,
    onButtonClick: () -> Unit
) {
    Column(modifier) {
        Text(text = "$number", fontSize = 72.sp)
        Button(onClick = onButtonClick) {
            Text(text = label)
        }
    }
}

@Composable
fun TicketPage() {
    // Semua state dikelola oleh parent
    var hargaTiket by rememberSaveable {
        mutableStateOf(50000)
    }

    var jumlahTiket by rememberSaveable {
        mutableStateOf(1)
    }

    var namaPembeli by rememberSaveable {
        mutableStateOf("")
    }

    var status by rememberSaveable {
        mutableStateOf("Menunggu pesanan")
    }

    var sedangMemproses by rememberSaveable {
        mutableStateOf(false)
    }

    var idPesanan by rememberSaveable {
        mutableStateOf(0)
    }

    // Status awal ketika nama masih kosong
    LaunchedEffect(Unit) {
        if (namaPembeli.isBlank()) {
            status = "Nama Masih Kosong"
        }
    }

    // Berjalan setiap kali tombol pesan ditekan
    LaunchedEffect(idPesanan) {
        if (idPesanan > 0) {
            status = "Memproses pesanan........."
            sedangMemproses = true

            delay(5000)

            status = "Tiket telah dipesan"
            sedangMemproses = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(Color(0xFF1565C0))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Pemesanan Tiket",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TicketContent(
                hargaTiket = hargaTiket,
                jumlahTiket = jumlahTiket,
                namaPembeli = namaPembeli,
                status = status,
                sedangMemproses = sedangMemproses,
                onNamaChange = {
                    namaPembeli = it
                },
                onKurangiJumlah = {
                    if (jumlahTiket > 1) {
                        jumlahTiket--
                    }
                },
                onTambahJumlah = {
                    jumlahTiket++
                },
                onPesanTiket = {
                    if (namaPembeli.isBlank()) {
                        status = "Nama Masih Kosong"
                    } else {
                        idPesanan++
                    }
                }
            )
        }
    }
}

@Composable
fun TicketContent(
    hargaTiket: Int,
    jumlahTiket: Int,
    namaPembeli: String,
    status: String,
    sedangMemproses: Boolean,
    onNamaChange: (String) -> Unit,
    onKurangiJumlah: () -> Unit,
    onTambahJumlah: () -> Unit,
    onPesanTiket: () -> Unit
) {
    val totalHarga = hargaTiket * jumlahTiket

    val warnaStatus = when {
        status == "Tiket telah dipesan" -> Color(0xFFE8F5E9)
        status.startsWith("Memproses") -> Color(0xFFE3F2FD)
        status == "Nama Masih Kosong" -> Color(0xFFFFEBEE)
        else -> Color(0xFFF3F6FA)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Nama Pembeli",
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = namaPembeli,
            onValueChange = onNamaChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Masukkan nama Anda")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )

        Text(
            text = "Harga Tiket",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = formatRupiah(hargaTiket),
            fontSize = 20.sp,
            color = Color(0xFF1565C0)
        )

        Text(
            text = "Jumlah Tiket",
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onKurangiJumlah,
                enabled = jumlahTiket > 1,
                modifier = Modifier.width(100.dp)
            ) {
                Text("-")
            }

            Text(
                text = jumlahTiket.toString(),
                modifier = Modifier.width(48.dp),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onTambahJumlah,
                modifier = Modifier.width(100.dp)
            ) {
                Text("+")
            }
        }

        Text(
            text = "Total: ${formatRupiah(totalHarga)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = onPesanTiket,
            enabled = !sedangMemproses,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (sedangMemproses) {
                    "Memproses..."
                } else {
                    "Pesan Tiket"
                }
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = warnaStatus
            )
        ) {
            Text(
                text = "Status: $status",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun formatRupiah(nilai: Int): String {
    val format = NumberFormat.getNumberInstance(
        Locale("id", "ID")
    )

    return "Rp ${format.format(nilai)}"
}