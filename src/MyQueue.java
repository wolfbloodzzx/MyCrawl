/**
 * Title:爬虫队列
 * Description:构建爬虫的url队列。本版本使用链表实现队列（尚无权值排序）
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 */

import java.util.LinkedList;

public class MyQueue {
	private LinkedList queue = new LinkedList();

	// 入队列
	public void enQueue(Object t) {
		queue.addLast(t);
	}

	// 出队列
	public Object deQueue() {
		return queue.removeFirst();
	}

	// 判断队列是否为空
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	// 判断队列是否包含t
	public boolean contians(Object t) {
		return queue.contains(t);
	}

	public boolean empty() {
		return queue.isEmpty();
	}

}
