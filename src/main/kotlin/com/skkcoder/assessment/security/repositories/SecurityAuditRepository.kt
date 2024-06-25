package com.skkcoder.assessment.security.repositories

import com.skkcoder.assessment.security.domain.SecurityAuditInformation
import java.util.*
import org.springframework.data.repository.CrudRepository

interface SecurityAuditRepository : CrudRepository<SecurityAuditInformation, UUID>