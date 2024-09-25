package com.example.beautycardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beautycardapp.ui.theme.BeautycardappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BeautyProductApp()
                }
            }
        }
    }
}

//data class for holding
data class Product(val name: String, val description: String, val price: Double, val imageRes: Int, val quantity: Int)


@Composable
/*
Manages the primary state and navigation within the app
Central point for handling whether the cart screen or product detail screen is displayed

State Management:
    Select different products
    Back pressed
 */
fun BeautyProductApp(){

    //Boolean state that determine if the cart screen should be shown (true) or the product detail screen (false)
    var showCart by remember { mutableStateOf(true) }

    //data class Product for "holding the currently selected product when chosen from the cart" null for not settled
    var selectedProduct by remember { mutableStateOf<Product?>(null)}


    //Conditional Logic: if showCart is true display CartScreen() if false  ProductDetailScreen()
    if(showCart){
        CartScreen(
            onProductSelected = {
                product -> selectedProduct = product
                showCart = false
            }
        )
    } else {
        selectedProduct?.let {
            product -> ProductDetailScreen(
                product = product,
                onBackPressed = {showCart = true}
            )
        }
    }
}


@Composable
//no return : -> Unit (similar to void)
/*
Displays a list of products available in the cart and provides options for checkout
Allows users to view and interact with the products in their cart - add promo code, proceeding to checkout
A hardcoded list of Product objects representing items in the cart (use images)
Column, text, lazy column, input field, button
onProductSelected callback - passed a parameter to handle product selection events.
 */
fun CartScreen(onProductSelected: (Product) -> Unit){

    //populate the items here (Hardcoded, should be dynamically)
    val products = listOf(
        Product("Make up fix", "for all skin type", 47.99, R.drawable.makeup_fix, 5),
        Product("Lotion", "night only", 97.99, R.drawable.cleansing_foam, 3),
        Product("Cleansing Foam", "for oily skin", 17.99, R.drawable.cleansing_foam_2, 2)
    )

    //UI Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        //Children for Column
        Text("Cart", style = MaterialTheme.typography.headlineMedium)
        Text("Product List", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))

        //lazy column for scrolling
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) {
                product -> ProductItem(product = product, onClick = {onProductSelected(product)} )
            }
        }

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(" Enter promo code ") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)

        )

        Button(
            onClick = { /* implement for checkout logics */}
        ){
            Text("Check out for *** dollars")
        }
    }
}


@Composable
fun ProductDetailScreen(product: (Product), onBackPressed: () -> Unit){

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        IconButton(onClick = onBackPressed){
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Back")
        }

        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)){
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(product.name, style = MaterialTheme.typography.headlineSmall)
                Text("$${product.price}", style = MaterialTheme.typography.headlineSmall)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                repeat(5){
                    index -> Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = if(index < 4) Color.Yellow else Color.Gray
                    )
                }

                Text("4.5", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
                Text("(5 Reviews)", color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
            }

            Text("Description", color = Color.Gray, modifier = Modifier.padding(top = 16.dp))
            Text("Comment", color = Color.Gray)

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row{
                    IconButton(onClick = {/* Decrease Quantity */}){
                        Text(" - ", fontSize = 24.sp)
                    }
                    Text("1", modifier = Modifier.padding(horizontal = 16.dp))
                    IconButton(onClick = {/* Increase Quantity */}){
                        Text(" + ", fontSize = 24.sp)
                    }
                }

                Button(onClick = {/* Add to the cart */}){
                    Text("Add to the Cart")
                }

            }
        }
    }
}

@Composable
//represent a single product item within the cart
/*
provide a reusable component for displaying product details and handling click events
Row layout
Image , for clip modifier


 */
fun ProductItem(product: (Product), onClick: () -> Unit ){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ){
            Text(product.name, fontWeight = FontWeight.Bold)
            Text(product.description, color = Color.Gray)
            Text("$${product.price}", fontWeight = FontWeight.Bold)

        }

        Text(
            "$${product.quantity}",
            modifier = Modifier
                .background(color = Color.White)
        )
    }
}

@Preview(showBackground = true)// for Preview
@Composable
fun DefaultPreview() {
    BeautycardappTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF04808C)

        ) {
            BeautyProductApp()
        }
    }
}