package lrucache

import java.util.concurrent.ConcurrentHashMap

case class Node[V](value: V) {
  var prev: Node[V] = _
  var next: Node[V] = _
}

case class LRUCache[K, V](max: Int) {
  private val index = new ConcurrentHashMap[K, Node[V]]()
  private var head: Node[V] = _
  private var last: Node[V] = _

  def put(key: K, value: V): Unit = {
    val n = Node(value)
    if (head != null) {
      n.next = head
      head.prev = n
    } else {
      last = n
    }

    head = n
    index.put(key, n)

    if (index.size > max) {
      index.remove(last.value)
      last = last.prev
      last.next = null
    }
  }

  def get(key: K): Option[V] = {
    Option(index.get(key)).map { n =>
      if (n.prev != null) {
        if (n.next != null) n.next.prev = n.prev
        n.prev.next = n.next
        n.prev = null
        n.next = head
        head = n
      }
      n.value
    }
  }


}
