package com.example.ludoarenabd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF090E1C)
private val Panel = Color(0xFF151E33)
private val Panel2 = Color(0xFF1D2944)
private val Purple = Color(0xFF9875FF)
private val Mint = Color(0xFF46D6A3)
private val Muted = Color(0xFFAAB5CC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LudoArenaApp() }
    }
}

private enum class Tab(val title: String) {
    HOME("হোম"), TOURNAMENT("টুর্নামেন্ট"), PLAY("খেলুন"), WALLET("ওয়ালেট"), PROFILE("প্রোফাইল")
}

@Composable
fun LudoArenaApp() {
    var selected by remember { mutableStateOf(Tab.HOME) }
    MaterialTheme(colorScheme = darkColorScheme(
        primary = Purple, secondary = Mint, background = Bg, surface = Panel,
        onPrimary = Color.White, onBackground = Color.White, onSurface = Color.White
    )) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Color.White) {
                    Tab.values().forEach { tab ->
                        val icon = when (tab) {
                            Tab.HOME -> Icons.Default.Home
                            Tab.TOURNAMENT -> Icons.Default.EmojiEvents
                            Tab.PLAY -> Icons.Default.SportsEsports
                            Tab.WALLET -> Icons.Default.AccountBalanceWallet
                            Tab.PROFILE -> Icons.Default.Person
                        }
                        NavigationBarItem(
                            selected = selected == tab,
                            onClick = { selected = tab },
                            icon = { Icon(icon, contentDescription = tab.title) },
                            label = { Text(tab.title, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Purple, selectedTextColor = Purple,
                                indicatorColor = Panel2, unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when (selected) {
                    Tab.HOME -> HomeScreen(onTournament = { selected = Tab.TOURNAMENT }, onWallet = { selected = Tab.WALLET }, onPlay = { selected = Tab.PLAY })
                    Tab.TOURNAMENT -> TournamentScreen()
                    Tab.PLAY -> PlayScreen()
                    Tab.WALLET -> WalletScreen()
                    Tab.PROFILE -> ProfileScreen()
                }
            }
        }
    }
}

@Composable
private fun ScreenColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Bg).padding(horizontal = 18.dp, vertical = 16.dp),
        content = content
    )
}

@Composable
private fun HomeScreen(onTournament: () -> Unit, onWallet: () -> Unit, onPlay: () -> Unit) {
    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("🇧🇩 LUDO ARENA", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                Text("স্বাগতম, খেলোয়াড়!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("আজকের খেলায় প্রস্তুত তো?", color = Muted, fontSize = 13.sp)
            }
            Surface(shape = CircleShape, color = Panel2, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Notifications, contentDescription = "নোটিফিকেশন") }
            }
        }
        Spacer(Modifier.height(18.dp))
        WalletHero(onWallet)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickAction("টুর্নামেন্ট", Icons.Default.EmojiEvents, onTournament, Modifier.weight(1f))
            QuickAction("এখনই খেলুন", Icons.Default.SportsEsports, onPlay, Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))
        SectionHeader("চলমান টুর্নামেন্ট", "সব দেখুন", onTournament)
        TournamentCard("ডেইলি লুডো অ্যারেনা", "৮ জন • পয়েন্ট ভিত্তিক", "রেজিস্ট্রেশন চলছে", "বিস্তারিত")
        Spacer(Modifier.height(16.dp))
        SectionHeader("শীর্ষ খেলোয়াড়", "লিডারবোর্ড", onTournament)
        LeaderRow("১", "Arena Player", "১,২৫০ পয়েন্ট")
        LeaderRow("২", "Ludo Star", "৯৮০ পয়েন্ট")
    }
}

@Composable
private fun WalletHero(onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel2), shape = RoundedCornerShape(20.dp), onClick = onClick) {
        Column(Modifier.fillMaxWidth().padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("আমার ওয়ালেট", color = Muted, modifier = Modifier.weight(1f))
                Icon(Icons.Default.AccountBalanceWallet, tint = Purple, contentDescription = null)
            }
            Spacer(Modifier.height(6.dp))
            Text("৳ ০.০০", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text("বাংলাদেশি টাকা (BDT)", color = Muted, fontSize = 11.sp)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = Purple)) { Text("ডিপোজিট") }
                OutlinedButton(onClick = onClick) { Text("উইথড্র") }
            }
        }
    }
}

@Composable
private fun QuickAction(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier) {
    Card(modifier.clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, tint = Purple, modifier = Modifier.size(26.dp), contentDescription = null)
            Spacer(Modifier.height(7.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(action, color = Purple, fontSize = 12.sp, modifier = Modifier.clickable { onClick() })
    }
}

@Composable
private fun TournamentCard(title: String, detail: String, status: String, action: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFF302653), shape = RoundedCornerShape(10.dp)) {
                    Icon(Icons.Default.EmojiEvents, tint = Purple, modifier = Modifier.padding(10.dp), contentDescription = null)
                }
                Spacer(Modifier.width(11.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold)
                    Text(detail, color = Muted, fontSize = 11.sp)
                }
                Text("●", color = Mint)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(status, color = Mint, fontSize = 11.sp, modifier = Modifier.weight(1f))
                OutlinedButton(onClick = {}, contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)) { Text(action, fontSize = 11.sp) }
            }
        }
    }
}

@Composable
private fun LeaderRow(rank: String, name: String, points: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(color = Panel2, shape = CircleShape, modifier = Modifier.size(30.dp)) {
            Box(contentAlignment = Alignment.Center) { Text(rank, fontWeight = FontWeight.Bold, color = Purple) }
        }
        Text(name, modifier = Modifier.weight(1f).padding(start = 10.dp), fontWeight = FontWeight.Medium)
        Text(points, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun TournamentScreen() {
    ScreenColumn {
        Text("টুর্নামেন্ট", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("আপনার পছন্দের ম্যাচ খুঁজে নিন", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = true, onClick = {}, label = { Text("সব") })
            FilterChip(selected = false, onClick = {}, label = { Text("আসন্ন") })
            FilterChip(selected = false, onClick = {}, label = { Text("চলমান") })
            FilterChip(selected = false, onClick = {}, label = { Text("শেষ") })
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf(
                Triple("ডেইলি অ্যারেনা", "৮ জন • পয়েন্ট টুর্নামেন্ট", "রেজিস্ট্রেশন চলছে"),
                Triple("উইকেন্ড কাপ", "১৬ জন • ৪ প্লেয়ার ম্যাচ", "শিগগিরই শুরু"),
                Triple("রুকি লিগ", "৩২ জন • নতুন খেলোয়াড়", "আসন্ন")
            )) { item -> TournamentCard(item.first, item.second, item.third, "বিস্তারিত") }
        }
    }
}

@Composable
private fun PlayScreen() {
    ScreenColumn {
        Text("খেলুন", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("গেম মোড নির্বাচন করুন", color = Muted)
        Spacer(Modifier.height(18.dp))
        TournamentCard("দ্রুত ম্যাচ", "দ্রুত প্রতিপক্ষ খুঁজুন", "ডেমো মোড", "নির্বাচন")
        Spacer(Modifier.height(12.dp))
        TournamentCard("বন্ধুদের সাথে", "প্রাইভেট রুম ফিচার", "শিগগিরই আসছে", "বিস্তারিত")
        Spacer(Modifier.height(12.dp))
        TournamentCard("টুর্নামেন্ট ম্যাচ", "নিবন্ধিত টুর্নামেন্টের ম্যাচ", "শিডিউল অনুযায়ী", "দেখুন")
        Spacer(Modifier.height(18.dp))
        Text("নোট: এই ভার্সনে আসল Ludo game engine এখনো যুক্ত হয়নি।", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun WalletScreen() {
    ScreenColumn {
        Text("আমার ওয়ালেট", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("বাংলাদেশি টাকা • BDT (৳)", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(18.dp))
        WalletHero {}
        Spacer(Modifier.height(18.dp))
        Text("পেমেন্ট মাধ্যম", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        PaymentMethod("বিকাশ", "bKash", Color(0xFFE2136E))
        Spacer(Modifier.height(9.dp))
        PaymentMethod("নগদ", "Nagad", Color(0xFFF2A900))
        Spacer(Modifier.height(18.dp))
        Text("সাম্প্রতিক লেনদেন", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.ReceiptLong, tint = Muted, modifier = Modifier.size(30.dp), contentDescription = null)
                Text("এখনো কোনো লেনদেন নেই", color = Muted, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("ডেমো UI: বাস্তব ডিপোজিট/উইথড্র এখনো সক্রিয় নয়।", color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun PaymentMethod(name: String, brand: String, color: Color) {
    Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = color, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(38.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(brand.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
            }
            Text(name, modifier = Modifier.weight(1f).padding(start = 12.dp), fontWeight = FontWeight.Bold)
            Text("Gateway সংযোগ বাকি", color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ProfileScreen() {
    ScreenColumn {
        Text("আমার প্রোফাইল", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Surface(color = Panel, shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(color = Panel2, shape = CircleShape, modifier = Modifier.size(76.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, modifier = Modifier.size(42.dp), tint = Purple, contentDescription = null) }
                }
                Spacer(Modifier.height(10.dp))
                Text("Guest Player", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text("Player ID: DEMO-0001", color = Muted, fontSize = 11.sp)
                Spacer(Modifier.height(8.dp))
                Text("লগইন সংযোগ পরবর্তী ধাপে যুক্ত হবে", color = Muted, fontSize = 11.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
        listOf("ব্যক্তিগত তথ্য", "ম্যাচ হিস্টোরি", "নোটিফিকেশন", "ভাষা: বাংলা", "সিকিউরিটি সেটিংস", "সাহায্য ও সাপোর্ট", "লগইন / রেজিস্ট্রেশন").forEach { label ->
            Surface(color = Panel, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(label, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, tint = Muted, contentDescription = null)
                }
            }
        }
    }
}
