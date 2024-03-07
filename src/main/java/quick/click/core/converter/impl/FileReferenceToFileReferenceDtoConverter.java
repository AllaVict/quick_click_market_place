package quick.click.core.converter.impl;

import org.springframework.stereotype.Component;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.FileReferenceDto;
import quick.click.core.domain.model.FileReference;

@Component
public class FileReferenceToFileReferenceDtoConverter implements TypeConverter<FileReference, FileReferenceDto> {

    @Override
    public Class<FileReference> getSourceClass() {
        return FileReference.class;
    }

    @Override
    public Class<FileReferenceDto> getTargetClass() {
        return FileReferenceDto.class;
    }

    @Override
    public FileReferenceDto convert(final FileReference fileReference) {
        final FileReferenceDto fileReferenceDto = new FileReferenceDto();
        fileReferenceDto.setId(fileReference.getId());
        fileReferenceDto.setUuid(fileReference.getUuid());
        fileReferenceDto.setFileName(fileReference.getFileName());
        fileReferenceDto.setFileType(fileReference.getFileType());

        return fileReferenceDto;
    }

}
