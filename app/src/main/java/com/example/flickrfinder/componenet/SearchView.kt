package com.example.flickrfinder.componenet

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun SearchView(
    navigationViewModel: PhotoViewModel,
    onSubmitSearch: (searchText: String) -> Unit
) {
    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = navigationViewModel.searchItemValue,
        queryLabel = "Search",
        onQueryChanged = { text ->
            navigationViewModel.updateSearchItem(text)
        },
        predictions = navigationViewModel.queryList,
        onClearClick = {
            navigationViewModel.updateSearchItem("")
        },
        onDoneActionClick = {
            onSubmitSearch(navigationViewModel.searchItemValue)
        },
        onItemClick = { text ->
            navigationViewModel.updateSearchItem(text)
        }
    ) {
        Text(
            text = it,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 14.sp
        )
    }

}

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {

    Column(modifier = Modifier.fillMaxSize()) {
        val view = LocalView.current

        QuerySearch(
            query = query,
            label = queryLabel,
            onQueryChanged = onQueryChanged,
            onDoneActionClick = {
                view.clearFocus()
                onDoneActionClick()
            },
            onClearClick = {
                onClearClick()
            }
        )

        val lazyListState = rememberLazyListState()
        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .heightIn(max = TextFieldDefaults.MinHeight * 6)
                .padding(top = 5.dp)
        ) {

            if (predictions.isNotEmpty()) {
                items(predictions) { prediction ->
                    Row(
                        Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 5.dp)
                            .fillMaxWidth()
                            .clickable {
                                onItemClick(prediction)
                            }
                    ) {
                        itemContent(prediction)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit
) {

    var showClearButton by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxChar = 15

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            }
            .focusRequester(focusRequester),
        value = query,
        onValueChange = {
            val allowedString = it.isAllowed()
            if (allowedString.length <= maxChar) onQueryChanged(allowedString)
        },
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        trailingIcon = {
            if (showClearButton) {
                IconButton(onClick = { onClearClick() }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            }
        },
        keyboardActions = KeyboardActions(onDone = {
            focusRequester.freeFocus()
            keyboardController?.hide()
            Handler(Looper.getMainLooper()).postDelayed({       // handler added to hide keyboard correctly
                onDoneActionClick()
            }, 0)
        }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )

}

private fun String.isAllowed() = filter { it.isLetter() }