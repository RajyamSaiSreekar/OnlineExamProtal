package com.onlineexam.analytics.service;

import com.onlineexam.analytics.model.Response;
import com.onlineexam.analytics.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    public Response saveResponse(Response response) {
        return responseRepository.save(response);
    }

    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }

    public List<Response> getResponsesByUserAndExam(Long userId, Long examId) {
        return responseRepository.findByUserIdAndExamId(userId, examId);
    }
}
