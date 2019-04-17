package com.darwino.admin.app.tree;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.darwino.commons.util.StringUtil;

@ApplicationScoped
public class DatabasesTreeLabelProvider extends LabelProvider implements ILabelProvider, ITableColorProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		return StringUtil.toString(element);
	}

	@Override
	public Color getBackground(Object paramObject, int paramInt) {
		return null;
	}

	@Override
	public Color getForeground(Object paramObject, int paramInt) {
		return null;
	}
}
