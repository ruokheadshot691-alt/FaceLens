package com.tracelens.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tracelens.app.ui.vm.SearchViewModel

@Composable
fun HomeScreen(
    vm: SearchViewModel,
    onFace: () -> Unit, onImage: () -> Unit, onAnalyze: () -> Unit, onCollections: () -> Unit, onSettings: () -> Unit,
) {
    val cols by vm.collections.collectAsStateWithLifecycle()
    val images = cols.sumOf { it.done }
    val faces = cols.sumOf { it.faces }

    TLScaffold(
        title = "TraceLens",
        onBack = null,
        actions = { IconButton(onClick = onSettings) { Icon(Icons.Filled.Settings, contentDescription = "Pengaturan") } },
    ) { pad ->
        Column(
            Modifier.fillMaxSize().padding(pad).padding(horizontal = 18.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(Modifier.height(4.dp))
            Eyebrow("LOCAL IMAGE INTELLIGENCE")
            Text("Temukan jejak gambar.", style = MaterialTheme.typography.displaySmall)
            Text(
                "Cari kemiripan wajah dan gambar dari koleksi yang kamu pilih. Pemrosesan inti tetap di perangkat.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(4.dp))
            Text("TOOLS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            BigButton("FACE SEARCH", "Cari kemiripan wajah di koleksi lokal", onFace)
            BigButton("IMAGE SEARCH", "Temukan gambar identik, crop, atau versi terkompresi", onImage)
            BigButton("ANALYZE IMAGE", "EXIF, hash, wajah, dan OCR offline", onAnalyze)

            Spacer(Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Stat("$images", "gambar")
                        Stat("$faces", "wajah")
                        Stat("${cols.size}", "koleksi")
                    }
                    Text(
                        "Index tersimpan lokal. Foto asli tidak disentuh saat indexing.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            BigButton("COLLECTIONS", "Kelola sumber gambar dan index", onCollections, primary = false)
            Text(
                "Similarity = kemiripan visual, bukan identifikasi nama seseorang.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 22.dp),
            )
        }
    }
}

@Composable
private fun RowScope.Stat(value: String, label: String) {
    Column(Modifier.weight(1f)) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
