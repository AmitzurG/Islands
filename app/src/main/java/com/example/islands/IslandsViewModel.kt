package com.example.islands

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.random.Random

class IslandsViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var grid: Array<IntArray>

    val pixelsGridBitmap: Bitmap
        get() {
            val h = grid.size
            val w = grid[0].size
            val pixelsBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            var pixelValue: Int
            for (m in 0 until h) {
                for (n in 0 until w) {
                    pixelValue = if (grid[m][n] == 0) Color.BLACK else Color.WHITE
                    pixelsBitmap.setPixel(n, m, pixelValue)
                }
            }
            return pixelsBitmap
        }

    val islandsGridBitmap: Bitmap
        get() {
            val h = grid.size
            val w = grid[0].size
            val islandsBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            var pixelValue: Int
            for (m in 0 until h) {
                for (n in 0 until w) {
                    pixelValue = if (grid[m][n] == 0) Color.BLACK else islandColor(grid[m][n])
                    islandsBitmap.setPixel(n, m, pixelValue)
                }
            }
            return islandsBitmap
        }


    fun createRandom01Grid(m: Int, n: Int) {
        grid = Array(m) { IntArray(n) }
        var random = Random.nextInt(0, 2)
        for (x in 0 until m) {
            for (y in 0 until n) {
                grid[x][y] = random
                random= Random.nextInt(0, 2)
            }
        }
    }

    fun calculateNumOfIslands() = liveData(Dispatchers.Default) {
        var count = 0
        var islandIndex = 2
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                if (grid[i][j] == 1) {
                    grid[i][j] = islandIndex
                    findIsland(i, j, islandIndex)
                    count++
                    islandIndex++
                }
            }
        }
        emit(count)
    }

    // recursive finsIsland(..) function, causes to stack overflow

//    private fun findIsland(i: Int, j: Int, islandIndex: Int) {
//        val delta = arrayOf(-1, 0, 1)
//        for (di in delta) {
//            for (dj in delta) {
//                if (i + di == i && j + dj == j) continue // not calculate on the pixel itself, only its neighbours
//                if (i + di >= 0 && i + di < grid.size && j + dj >= 0 && j + dj < grid[0].size && grid[i + di][j + dj] == 1) {
//                    grid[i + di][j + dj] = islandIndex
//                    findIsland(i + di, j + dj, islandIndex)
//                }
//            }
//        }
//    }

    private fun findIsland(i: Int, j: Int, islandIndex: Int) {
        val queue = LinkedList<Pair<Int, Int>>()
        val delta = arrayOf(-1, 0, 1)
        queue.offer(Pair(i, j))
        while (queue.isNotEmpty()) {
            val position = queue.poll()
            val x = position?.first ?: 0; val y = position?.second ?: 0
            for (dx in delta) {
                for (dy in delta) {
                    if (dx == 0 && dy == 0) continue // not calculate on the pixel itself, only its neighbours
                    if (x + dx >= 0 && x + dx < grid.size && y + dy >= 0 && y + dy < grid[0].size &&
                            grid[x + dx][y + dy] == 1) {
                        queue.offer(Pair(x + dx, y + dy))
                        grid[x + dx][y + dy] = islandIndex
                    }
                }
            }
        }
    }

    private val colorMap = HashMap<Int, Int>()
    private fun islandColor(index: Int): Int {
        colorMap[index] = colorMap.getOrDefault(index, Color.rgb(Random.nextInt(0xff), Random.nextInt(0xff), Random.nextInt(0xff)))
        return colorMap[index] ?: Color.WHITE
    }
}
