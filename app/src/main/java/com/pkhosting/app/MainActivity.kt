package com.pkhosting.app

import android.content.Intent
import android.content.ClipData
import android.widget.Toast
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF071B36)
private val Blue = Color(0xFF1769E8)
private val Ink = Color(0xFF152238)
private val Muted = Color(0xFF65748B)
private val SurfaceSoft = Color(0xFFF3F6FB)

class MainActivity : ComponentActivity() {
    private fun openSecurePage(url: String) {
        val uri = Uri.parse(url)
        try {
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(android.graphics.Color.rgb(7, 27, 54))
                .build()
                .launchUrl(this, uri)
        } catch (_: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val colors = lightColorScheme(
                primary = Blue,
                onPrimary = Color.White,
                background = SurfaceSoft,
                surface = Color.White,
                onSurface = Ink
            )
            MaterialTheme(colorScheme = colors) {
                PKHostingApp(
                    openPage = ::openSecurePage,
                    openPhone = { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:04235692952"))) },
                    openEmail = { startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:sales@pkhosting.com"))) }
                )
            }
        }
    }
}

private enum class Tab(val label: String) {
    HOME("Home"), HOSTING("Hosting"), DOMAINS("Domains"), SUPPORT("Support")
}

@Composable
private fun PKHostingApp(
    openPage: (String) -> Unit,
    openPhone: () -> Unit,
    openEmail: () -> Unit
) {
    var selected by remember { mutableStateOf(Tab.HOME) }
    BackHandler(enabled = selected != Tab.HOME) { selected = Tab.HOME }

    Scaffold(
        containerColor = SurfaceSoft,
        topBar = {
            Row(
                Modifier.fillMaxWidth().background(Navy).statusBarsPadding().padding(horizontal = 20.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(38.dp).background(Blue, RoundedCornerShape(11.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("PK", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp) }
                Spacer(Modifier.width(11.dp))
                Column {
                    Text("PKHosting", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                    Text("Quality-crafted hosting", color = Color(0xFFB8C9E1), fontSize = 11.sp)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { openPage("https://www.pkhosting.com/dashboard/") }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Customer account", tint = Color.White)
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                Tab.entries.forEach { tab ->
                    val icon = when (tab) {
                        Tab.HOME -> Icons.Default.Home
                        Tab.HOSTING -> Icons.Default.Cloud
                        Tab.DOMAINS -> Icons.Default.Language
                        Tab.SUPPORT -> Icons.Default.SupportAgent
                    }
                    NavigationBarItem(
                        selected = selected == tab,
                        onClick = { selected = tab },
                        icon = { Icon(icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        when (selected) {
            Tab.HOME -> HomeScreen(
                Modifier.padding(padding),
                onHosting = { selected = Tab.HOSTING },
                onDomains = { selected = Tab.DOMAINS },
                onSupport = { selected = Tab.SUPPORT },
                openPage = openPage
            )
            Tab.HOSTING -> HostingScreen(Modifier.padding(padding), openPage)
            Tab.DOMAINS -> DomainsScreen(Modifier.padding(padding), openPage)
            Tab.SUPPORT -> SupportScreen(Modifier.padding(padding), openPage, openPhone, openEmail)
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier,
    onHosting: () -> Unit,
    onDomains: () -> Unit,
    onSupport: () -> Unit,
    openPage: (String) -> Unit
) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Column(
                Modifier.fillMaxWidth().background(Navy, RoundedCornerShape(22.dp)).padding(22.dp)
            ) {
                Text("YOUR ONLINE HOME STARTS HERE", color = Color(0xFF9FC3FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Text("Build your business.
We'll host it.", color = Color.White, fontSize = 29.sp, lineHeight = 35.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(9.dp))
                Text("Reliable hosting, domains and server solutions from PK Hosting.", color = Color(0xFFD4DFEF), fontSize = 14.sp, lineHeight = 21.sp)
                Spacer(Modifier.height(18.dp))
                Button(onClick = onHosting, colors = ButtonDefaults.buttonColors(containerColor = Blue)) {
                    Text("Explore hosting plans")
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(17.dp))
                }
            }
        }
        item { SectionTitle("What do you need today?") }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionTile("Web hosting", "Fast SSD hosting", Icons.Default.Cloud, Modifier.weight(1f), onHosting)
                ActionTile("Find a domain", "Your name online", Icons.Default.Language, Modifier.weight(1f), onDomains)
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionTile("WordPress", "One-click setup", Icons.Default.Web, Modifier.weight(1f)) {
                    openPage("https://www.pkhosting.com/wordpress-hosting-pakistan/")
                }
                ActionTile("VPS servers", "Scale your workload", Icons.Default.Dns, Modifier.weight(1f)) {
                    openPage("https://www.pkhosting.com/pricing/")
                }
            }
        }
        item { SectionTitle("Hosting made simple") }
        item {
            InfoCard(
                title = "A plan for every stage",
                body = "Start with personal hosting, grow into business hosting, or choose professional resources.",
                action = "Compare all plans",
                onClick = onHosting
            )
        }
        item {
            InfoCard(
                title = "Already a customer?",
                body = "Access your secure PKHosting account to manage services, invoices and support.",
                action = "Open customer account",
                onClick = { openPage("https://www.pkhosting.com/clientarea.php") }
            )
        }
        item {
            OutlinedButton(onClick = onSupport, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.SupportAgent, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Talk to our support team")
            }
        }
        item { Text("PK Hosting · Lahore, Pakistan", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(vertical = 8.dp)) }
    }
}

@Composable
private fun HostingScreen(modifier: Modifier, openPage: (String) -> Unit) {
    val plans = listOf(
        Triple("Personal Hosting", "For students, hobbyists and individuals", "1 website · 5 GB SSD · 5 email accounts"),
        Triple("Business Hosting", "For growing businesses and teams", "5 websites · 25 GB SSD · 25 email accounts"),
        Triple("Professional Hosting", "For software houses and corporates", "10 websites · 50 GB SSD · 50 email accounts"),
        Triple("WP Starter", "For a single WordPress website", "7 GB SSD · One-click WordPress · Free SSL"),
        Triple("WP Business", "For growing WordPress sites", "3 websites · 20 GB SSD · Daily backups"),
        Triple("WP Pro", "For studios managing multiple sites", "10 websites · 30 GB SSD · Priority support")
    )
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
        item {
            PageHeading("Hosting plans", "Choose a package that fits your website. Live prices and order terms are shown on PKHosting.")
        }
        items(plans) { plan ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(17.dp)) {
                Column(Modifier.fillMaxWidth().padding(17.dp)) {
                    Text(plan.first, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(plan.second, color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
                    Text(plan.third, color = Ink, fontSize = 13.sp, modifier = Modifier.padding(top = 12.dp))
                    Text("SSL support · Managed dashboard · Support", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
                    Button(onClick = { openPage("https://www.pkhosting.com/pricing/") }, modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        Text("View live price & order")
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
        item {
            OutlinedButton(onClick = { openPage("https://www.pkhosting.com/pricing/") }, modifier = Modifier.fillMaxWidth()) {
                Text("View complete pricing catalogue")
            }
        }
    }
}

@Composable
private fun DomainsScreen(modifier: Modifier, openPage: (String) -> Unit) {
    var domain by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        item {
            PageHeading("Find your domain", "Search for a name for your business, project or personal website.")
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(18.dp)) {
                    Text("Your domain name", fontWeight = FontWeight.SemiBold, color = Ink)
                    OutlinedTextField(
                        value = domain,
                        onValueChange = { domain = it },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        placeholder = { Text("e.g. yourbusiness.com") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                    )
                    Button(onClick = {
                        val clipboard = context.getSystemService(android.content.ClipboardManager::class.java)
                        clipboard.setPrimaryClip(ClipData.newPlainText("Domain name", domain.trim()))
                        Toast.makeText(context, "Domain copied. Paste it into the live search.", Toast.LENGTH_LONG).show()
                        openPage("https://www.pkhosting.com/domains/")
                    }, enabled = domain.isNotBlank(), modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Continue to secure domain search")
                    }
                    Text("Availability and final registration are confirmed in PKHosting's live domain search and checkout.", color = Muted, fontSize = 12.sp, lineHeight = 17.sp, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
        item { SectionTitle("Popular extensions") }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(".com", ".pk", ".com.pk").forEach { ExtensionChip(it) }
            }
        }
        item {
            InfoCard("Domain management", "Manage renewals, nameservers and DNS from your secure customer dashboard.", "Open domain services") {
                openPage("https://www.pkhosting.com/domains/")
            }
        }
    }
}

@Composable
private fun SupportScreen(
    modifier: Modifier,
    openPage: (String) -> Unit,
    openPhone: () -> Unit,
    openEmail: () -> Unit
) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { PageHeading("We're here to help", "Contact the PKHosting team about hosting, domains or your account.") }
        item { InfoCard("Call PKHosting", "(042) 35692952", "Call now", openPhone) }
        item { InfoCard("Email support", "sales@pkhosting.com", "Send email", openEmail) }
        item { InfoCard("Support tickets", "Get help through your secure customer dashboard.", "Open support", { openPage("https://www.pkhosting.com/contact/") }) }
        item { InfoCard("Contact details", "Lahore, Pakistan · Monday to Friday, 9am–6pm PKT for live chat and direct line.", "View contact page", { openPage("https://www.pkhosting.com/contact/") }) }
    }
}

@Composable
private fun PageHeading(title: String, subtitle: String) {
    Column(Modifier.padding(bottom = 4.dp)) {
        Text(title, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
        Text(subtitle, fontSize = 13.sp, lineHeight = 19.sp, color = Muted, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
}

@Composable
private fun ActionTile(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(modifier.clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(15.dp)) {
            Icon(icon, contentDescription = null, tint = Blue, modifier = Modifier.size(25.dp))
            Text(title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 13.dp))
            Text(subtitle, color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
        }
    }
}

@Composable
private fun InfoCard(title: String, body: String, action: String, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(17.dp)) {
            Text(title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(body, color = Muted, fontSize = 13.sp, lineHeight = 19.sp, modifier = Modifier.padding(top = 6.dp))
            TextButton(onClick = onClick, modifier = Modifier.align(Alignment.End)) {
                Text(action)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(15.dp))
            }
        }
    }
}

@Composable
private fun ExtensionChip(label: String) {
    Surface(color = Color(0xFFE7EFFF), shape = RoundedCornerShape(30.dp)) {
        Text(label, color = Blue, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 15.dp, vertical = 9.dp))
    }
}
