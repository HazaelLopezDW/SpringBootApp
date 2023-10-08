package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	
	private String url;
	private Page<T> page;
	
	private int totalPaginas;
	private int numElementosPorPaginas;
	private int paginaActual;
	
	private List<PageItem> paginas;
	
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();
		
		this.numElementosPorPaginas = page.getSize();
		this.totalPaginas = page.getTotalPages();
		this.paginaActual = page.getNumber() + 1;
		
		int desde, hasta;
		
		if(this.totalPaginas <= this.numElementosPorPaginas) {
			desde = 1;
			hasta = this.totalPaginas;
		} else {
			if(this.paginaActual <= this.numElementosPorPaginas/2) {
				desde = 1;
				hasta = this.numElementosPorPaginas;
			} else if(this.paginaActual >= this.totalPaginas - this.numElementosPorPaginas/2) {
				desde = this.totalPaginas - this.numElementosPorPaginas + 1;
				hasta = this.numElementosPorPaginas;
			} else {
				desde = this.paginaActual - this.numElementosPorPaginas/2;
				hasta = this.numElementosPorPaginas;
			}
		}
		
		for(int i=0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, this.paginaActual == desde+i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}
	
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}