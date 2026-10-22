package com.sorsplus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CalculatorApp() }
    }
}

private enum class Screen { CALC, CONVERT, PHYSICS, CHEMISTRY, HISTORY }
private data class Calculation(val expression: String, val result: String)

@Composable
private fun CalculatorApp() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var screen by remember { mutableStateOf(Screen.CALC) }
    var english by remember { mutableStateOf(false) }
    var dark by remember { mutableStateOf(true) }
    var degrees by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("") }
    val history = remember { mutableStateListOf<Calculation>() }
    val title = if (english) "SORS+" else "SORS+"
    val bg = if (dark) androidx.compose.ui.graphics.Color(0xFF0B1020) else androidx.compose.ui.graphics.Color(0xFFF3F6FB)
    val panel = if (dark) androidx.compose.ui.graphics.Color(0xFF111A2E) else androidx.compose.ui.graphics.Color.White
    val ink = if (dark) androidx.compose.ui.graphics.Color(0xFFF4F7FF) else androidx.compose.ui.graphics.Color(0xFF152039)
    val accent = if (dark) androidx.compose.ui.graphics.Color(0xFF77F2C1) else androidx.compose.ui.graphics.Color(0xFF0DBB83)

    MaterialTheme(colorScheme = if (dark) darkColorScheme(primary = accent, background = bg, surface = panel, onSurface = ink, onBackground = ink) else lightColorScheme(primary = accent, background = bg, surface = panel, onSurface = ink, onBackground = ink)) {
        Surface(Modifier.fillMaxSize(), color = bg) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 14.dp, vertical = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                        Text(if (english) "Calculate smarter" else "Hitung lebih mudah", style = MaterialTheme.typography.labelSmall)
                    }
                    TextButton(onClick = { english = !english }) { Text(if (english) "ID" else "EN") }
                    TextButton(onClick = { dark = !dark }) { Text(if (dark) "☼" else "☾") }
                }
                Spacer(Modifier.height(10.dp))
                Card(colors = CardDefaults.cardColors(containerColor = panel), shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.End) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(when (screen) { Screen.CALC -> if (english) "SCIENTIFIC CALCULATOR" else "KALKULATOR ILMIAH"; Screen.CONVERT -> if (english) "UNIT CONVERTER" else "KONVERSI SATUAN"; Screen.PHYSICS -> if (english) "PHYSICS" else "FISIKA"; Screen.CHEMISTRY -> if (english) "CHEMISTRY" else "KIMIA"; Screen.HISTORY -> if (english) "HISTORY" else "RIWAYAT" }, style = MaterialTheme.typography.labelSmall)
                            if (screen == Screen.CALC) TextButton(onClick = { degrees = !degrees }) { Text(if (degrees) "DEG °" else "RAD") }
                        }
                        Spacer(Modifier.height(18.dp))
                        Text(expression.ifBlank { "0" }, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = .65f), textAlign = TextAlign.End)
                        Text(result, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                        if (message.isNotBlank()) Text(message, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                    }
                }
                Spacer(Modifier.height(10.dp))
                val labels = listOf(if (english) "Calc" else "Hitung", if (english) "Convert" else "Konversi", if (english) "Physics" else "Fisika", if (english) "Chemistry" else "Kimia", if (english) "History" else "Riwayat")
                val screens = Screen.values().toList()
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    screens.forEachIndexed { i, s ->
                        val selected = screen == s
                        Button(onClick = { screen = s; message = "" }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = if (selected) accent else panel, contentColor = if (selected) bg else ink)) {
                            Text(labels[i], style = MaterialTheme.typography.labelSmall, maxLines = 1)
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                when (screen) {
                    Screen.CALC -> CalculatorPad(english, onKey = { key ->
                        message = ""
                        when (key) {
                            "AC" -> { expression = ""; result = "0" }
                            "⌫" -> { expression = expression.dropLast(1); result = preview(expression, degrees) }
                            "=" -> try {
                                val n = ExpressionParser(expression, degrees).parse()
                                result = formatNumber(n); history.add(0, Calculation(expression, result)); if (history.size > 100) history.removeAt(history.lastIndex)
                            } catch (e: Exception) { message = e.message ?: if (english) "Invalid expression" else "Ekspresi tidak valid" }
                            "±" -> { expression = if (expression.startsWith("-")) expression.drop(1) else "-($expression)"; result = preview(expression, degrees) }
                            else -> { expression += key; result = preview(expression, degrees) }
                        }
                    })
                    Screen.CONVERT -> ConverterScreen(english, panel, ink)
                    Screen.PHYSICS -> PhysicsScreen(english, panel, ink)
                    Screen.CHEMISTRY -> ChemistryScreen(english, panel, ink)
                    Screen.HISTORY -> {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { TextButton(onClick = { history.clear() }) { Text(if (english) "Clear history" else "Hapus riwayat") } }
                        if (history.isEmpty()) Text(if (english) "No calculations yet." else "Belum ada perhitungan.", modifier = Modifier.fillMaxWidth().padding(24.dp), textAlign = TextAlign.Center)
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(history) { item -> Card(colors = CardDefaults.cardColors(containerColor = panel), modifier = Modifier.fillMaxWidth(), onClick = { expression = item.expression; result = item.result; screen = Screen.CALC }) { Column(Modifier.padding(12.dp)) { Text(item.expression); Text("= ${item.result}", fontWeight = FontWeight.Bold) } } } }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(if (english) "Offline • Calculations run on this device" else "Offline • Perhitungan berjalan di perangkat", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun CalculatorPad(english: Boolean, onKey: (String) -> Unit) {
    val rows = listOf(
        listOf("AC", "⌫", "(", ")"),
        listOf("sin(", "cos(", "tan(", "÷"),
        listOf("sqrt(", "log(", "ln(", "×"),
        listOf("7", "8", "9", "−"),
        listOf("4", "5", "6", "+"),
        listOf("1", "2", "3", "^"),
        listOf("±", "0", ".", "="),
        listOf("π", "e", "%", "!")
    )
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        rows.forEach { row -> Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
            row.forEach { key ->
                val isEquals = key == "="; val isAction = key in listOf("AC", "⌫")
                Button(onClick = { onKey(key) }, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(14.dp), contentPadding = PaddingValues(1.dp), colors = ButtonDefaults.buttonColors(containerColor = if (isEquals) MaterialTheme.colorScheme.primary else if (isAction) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant, contentColor = if (isEquals) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)) {
                    Text(if (key == "AC" && english) "CLR" else key, fontWeight = FontWeight.SemiBold)
                }
            }
        } }
        Text(if (english) "Use parentheses for function inputs, e.g. sin(30)." else "Gunakan kurung untuk fungsi, contoh: sin(30).", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun ConverterScreen(english: Boolean, panel: androidx.compose.ui.graphics.Color, ink: androidx.compose.ui.graphics.Color) {
    var category by remember { mutableStateOf("Length") }; var amount by remember { mutableStateOf("1") }; var from by remember { mutableStateOf("m") }; var to by remember { mutableStateOf("cm") }
    val units = when (category) {
        "Mass" -> listOf("kg", "g", "mg", "lb")
        "Temperature" -> listOf("°C", "°F", "K")
        else -> listOf("m", "cm", "mm", "km", "in", "ft")
    }
    LaunchedEffect(category) { from = units.first(); to = units.getOrElse(1) { units.first() } }
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (english) "Category" else "Kategori", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("Length", "Mass", "Temperature").forEach { c -> FilterChip(selected = category == c, onClick = { category = c }, label = { Text(when (c) { "Length" -> if (english) "Length" else "Panjang"; "Mass" -> if (english) "Mass" else "Massa"; else -> if (english) "Temperature" else "Suhu" }) }) } }
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text(if (english) "Value" else "Nilai") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            UnitPicker(if (english) "From" else "Dari", from, units, { from = it }, Modifier.weight(1f))
            UnitPicker(if (english) "To" else "Ke", to, units, { to = it }, Modifier.weight(1f))
        }
        val converted = runCatching { convert(amount.toDouble(), category, from, to) }
            .getOrNull()
            ?.takeIf { it.isFinite() }
        Card(colors = CardDefaults.cardColors(containerColor = panel), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text(if (english) "RESULT" else "HASIL KONVERSI", style = MaterialTheme.typography.labelSmall); Text(if (converted == null) "—" else "${formatNumber(converted)} $to", style = MaterialTheme.typography.headlineMedium, color = ink, fontWeight = FontWeight.Bold) } }
    }
}

@Composable
private fun UnitPicker(label: String, selected: String, options: List<String>, onSelect: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier) { Text(label, style = MaterialTheme.typography.labelSmall); Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(selected) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { options.forEach { option -> DropdownMenuItem(text = { Text(option) }, onClick = { onSelect(option); expanded = false }) } }
    } }
}

private fun convert(v: Double, category: String, from: String, to: String): Double {
    require(v.isFinite()) { "Nilai harus berupa angka yang valid" }
    if (category == "Temperature") {
        val c = when (from) { "°F" -> (v - 32) * 5 / 9; "K" -> v - 273.15; else -> v }
        return when (to) { "°F" -> c * 9 / 5 + 32; "K" -> c + 273.15; else -> c }
    }
    val factors = if (category == "Mass") mapOf("kg" to 1.0, "g" to .001, "mg" to .000001, "lb" to .45359237) else mapOf("m" to 1.0, "cm" to .01, "mm" to .001, "km" to 1000.0, "in" to .0254, "ft" to .3048)
    return v * factors.getValue(from) / factors.getValue(to)
}

@Composable
private fun PhysicsScreen(english: Boolean, panel: androidx.compose.ui.graphics.Color, ink: androidx.compose.ui.graphics.Color) {
    var distance by remember { mutableStateOf("100") }; var time by remember { mutableStateOf("20") }; var mass by remember { mutableStateOf("2") }; var acceleration by remember { mutableStateOf("9.8") }
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (english) "Basic physics formulas" else "Rumus fisika dasar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(distance, { distance = it }, label = { Text(if (english) "Distance (m)" else "Jarak (m)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(time, { time = it }, label = { Text(if (english) "Time (s)" else "Waktu (s)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        ResultCard(if (english) "Average speed = distance / time" else "Kelajuan rata-rata = jarak / waktu", runCatching { distance.toDouble() / time.toDouble() }.getOrNull(), "m/s", panel, ink)
        Divider()
        OutlinedTextField(mass, { mass = it }, label = { Text(if (english) "Mass (kg)" else "Massa (kg)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(acceleration, { acceleration = it }, label = { Text(if (english) "Acceleration (m/s²)" else "Percepatan (m/s²)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        ResultCard(if (english) "Force = mass × acceleration" else "Gaya = massa × percepatan", runCatching { mass.toDouble() * acceleration.toDouble() }.getOrNull(), "N", panel, ink)
        Text(if (english) "Check units and assumptions before using results in real-world engineering." else "Periksa satuan dan asumsi sebelum memakai hasil untuk rekayasa nyata.", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun ChemistryScreen(english: Boolean, panel: androidx.compose.ui.graphics.Color, ink: androidx.compose.ui.graphics.Color) {
    var mass by remember { mutableStateOf("18") }; var molarMass by remember { mutableStateOf("18.015") }; var concentration by remember { mutableStateOf("0.5") }; var volume by remember { mutableStateOf("0.25") }
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (english) "Basic chemistry calculations" else "Perhitungan kimia dasar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(if (english) "Amount of substance" else "Jumlah zat", style = MaterialTheme.typography.titleSmall)
        OutlinedTextField(mass, { mass = it }, label = { Text(if (english) "Mass (g)" else "Massa (g)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(molarMass, { molarMass = it }, label = { Text(if (english) "Molar mass (g/mol)" else "Massa molar (g/mol)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        ResultCard(if (english) "Moles = mass / molar mass" else "Mol = massa / massa molar", runCatching { mass.toDouble() / molarMass.toDouble() }.getOrNull(), "mol", panel, ink)
        Divider()
        Text(if (english) "Molar concentration" else "Konsentrasi molar", style = MaterialTheme.typography.titleSmall)
        OutlinedTextField(concentration, { concentration = it }, label = { Text(if (english) "Concentration (mol/L)" else "Konsentrasi (mol/L)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(volume, { volume = it }, label = { Text(if (english) "Volume (L)" else "Volume (L)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        ResultCard(if (english) "Moles = concentration × volume" else "Mol = konsentrasi × volume", runCatching { concentration.toDouble() * volume.toDouble() }.getOrNull(), "mol", panel, ink)
        Text(if (english) "Enter the molar mass for the compound you are using." else "Masukkan massa molar sesuai senyawa yang digunakan.", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun ResultCard(label: String, value: Double?, unit: String, panel: androidx.compose.ui.graphics.Color, ink: androidx.compose.ui.graphics.Color) {
    Card(colors = CardDefaults.cardColors(containerColor = panel), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(14.dp)) { Text(label, style = MaterialTheme.typography.labelMedium); Text(if (value == null || !value.isFinite()) "—" else "${formatNumber(value)} $unit", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = ink) } }
}

private fun preview(expression: String, degrees: Boolean): String = if (expression.isBlank()) "0" else runCatching { formatNumber(ExpressionParser(expression, degrees).parse()) }.getOrElse { "—" }
private fun formatNumber(n: Double): String { require(n.isFinite()) { "Hasil tidak terdefinisi" }; if (abs(n) < 1e-12) return "0"; return String.format(Locale.US, "%.10g", n) }

/** Recursive-descent parser. Does not use eval()/JavaScript; rejects trailing or malformed input. */
private class ExpressionParser(private val source: String, private val degrees: Boolean) {
    private val s = source.replace("×", "*").replace("÷", "/").replace("−", "-").replace("π", "pi")
    private var i = 0
    fun parse(): Double { val v = add(); spaces(); require(i == s.length) { "Periksa kembali ekspresi" }; require(v.isFinite()) { "Hasil di luar batas" }; return v }
    private fun spaces() { while (i < s.length && s[i].isWhitespace()) i++ }
    private fun eat(c: Char): Boolean { spaces(); if (i < s.length && s[i] == c) { i++; return true }; return false }
    private fun add(): Double {
        var value = multiply()
        while (true) {
            value = when {
                eat('+') -> value + multiply()
                eat('-') -> value - multiply()
                else -> return value
            }
        }
    }

    private fun multiply(): Double {
        var value = unary()
        while (true) {
            value = when {
                eat('*') -> value * unary()
                eat('/') -> {
                    val divisor = unary()
                    require(divisor != 0.0) { "Tidak bisa membagi dengan nol" }
                    value / divisor
                }
                else -> return value
            }
        }
    }

    private fun unary(): Double {
        if (eat('+')) return unary()
        if (eat('-')) return -unary()
        return power()
    }

    private fun power(): Double {
        val value = postfix()
        return if (eat('^')) value.pow(unary()) else value
    }

    private fun postfix(): Double {
        var value = primary()
        while (true) {
            when {
                eat('!') -> {
                    require(value >= 0 && value <= 170 && value % 1.0 == 0.0) { "Faktorial butuh bilangan bulat 0–170" }
                    var factorial = 1.0
                    for (factor in 2..value.toInt()) factorial *= factor
                    value = factorial
                }
                eat('%') -> value /= 100.0
                else -> return value
            }
        }
    }

    private fun primary(): Double {
        spaces(); require(i < s.length) { "Ekspresi belum lengkap" }
        if (eat('(')) { val v = add(); require(eat(')')) { "Kurung belum lengkap" }; return v }
        if (s[i].isDigit() || s[i] == '.') { val start = i; while (i < s.length && (s[i].isDigit() || s[i] == '.')) i++; if (i < s.length && s[i].lowercaseChar() == 'e') { val expStart = i; i++; if (i < s.length && s[i] in "+-") i++; val digits = i; while (i < s.length && s[i].isDigit()) i++; if (digits == i) i = expStart }; return s.substring(start, i).toDouble() }
        if (s[i].isLetter()) {
            val start = i; while (i < s.length && s[i].isLetter()) i++; val name = s.substring(start, i).lowercase(Locale.ROOT)
            if (name == "pi") return PI; if (name == "e") return E
            require(eat('(')) { "Fungsi perlu tanda kurung" }; val a = add(); require(eat(')')) { "Kurung belum lengkap" }
            val x = if (degrees) a * PI / 180.0 else a
            return when (name) { "sin" -> sin(x); "cos" -> cos(x); "tan" -> tan(x); "sqrt" -> sqrt(a); "log" -> log10(a); "ln" -> ln(a); "asin" -> asin(a).let { if (degrees) it * 180 / PI else it }; "acos" -> acos(a).let { if (degrees) it * 180 / PI else it }; "atan" -> atan(a).let { if (degrees) it * 180 / PI else it }; else -> error("Fungsi tidak dikenal: $name") }
        }
        error("Karakter tidak dikenal: ${s[i]}")
    }
}
