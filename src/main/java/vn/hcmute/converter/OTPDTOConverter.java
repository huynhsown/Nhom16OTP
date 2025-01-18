package vn.hcmute.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.model.dto.OTPDTO;

@Component
public class OTPDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public OTPDTO toOTPDTO(OTPEntity otpEntity){
        return modelMapper.map(otpEntity, OTPDTO.class);
    }

}
