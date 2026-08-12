package com.example.jobfusion

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TicTacToeScreen(boardSize: Int = 3) {


    var board by remember {
        mutableStateOf(List(boardSize*boardSize){""})
    }

    var currentPlayer by remember {
        mutableStateOf("X")
    }

    var gameOver by remember {
        mutableStateOf(false)
    }
    var status by remember {
        mutableStateOf("Player X's Turn")
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {


        Spacer(
            modifier = Modifier.height(50.dp)
        )
        Text(status)
        LazyVerticalGrid(
            columns = GridCells.Fixed(boardSize),
            modifier = Modifier.size((boardSize *90).dp),
            userScrollEnabled = false,
        ) {
            items(board.size) { index ->
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .border(1.dp, Color.Black)
                        .clickable {
                            if(board[index].isEmpty() && !gameOver) {
                                val newBoard = board.toMutableList()
                                newBoard[index] = currentPlayer
                                board = newBoard
                                when {
                                    hasWinner(board, boardSize, currentPlayer) ->{
                                        status = "Player $currentPlayer Wins"
                                        gameOver = true

                                    }
                                    else -> {
                                        currentPlayer =
                                            if(currentPlayer == "X") "0" else "X"
                                        status = "Player $currentPlayer turn"
                                    }

                                }

                            }
                        }
                ) {
                    Text(
                        text = board[index]
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
//        Button(
//            onClick = {
//                board = List(boardSize *boardSize)
//            } {
//                Text("")
//            }
//        )
    }
}

private fun hasWinner(board: List<String>, n: Int, player: String): Boolean {
    //rows
    for(row in 0 until n) {
        var win = true
        for(col in 0 until n) {
            if(board[row*n+col] != player) {
                win = false
                break
            }
        }
        if(win) return true
    }

    //columns
    for(col in 0 until n) {
        var win = true
        for(row in 0 until n) {
            if(board[row*n+col] != player) {
                win = false
                break
            }
        }
        if(win) return true
    }

    // diagona;s
    var win = true
    for(i in 0 until n) {
        if(board[i*n+i] != player) {
            win = false
            break
        }
    }
    if(win) return true

     win = true
        for(i in 0 until n) {
            if(board[i*n+(n-1-i)] != player) {
                win = false
                break
            }
        }


    return win
}