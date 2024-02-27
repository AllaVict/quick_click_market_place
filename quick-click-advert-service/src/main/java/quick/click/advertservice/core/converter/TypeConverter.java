package quick.click.advertservice.core.converter;

import java.util.Collection;
import java.util.List;

public interface TypeConverter<S, T> {

	Class<S> getSourceClass();

	Class<T> getTargetClass();

	T convert(S source);

	default List<T> convert(final Collection<S> sourceList) {
		return sourceList
			.stream()
			.map(this::convert)
			.toList();
	}

}
