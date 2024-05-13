UPDATE
   tbd_CadastroLembretes
SET
   tp_Baixado = 'S'
WHERE
   id_Lembrete = :idLembrete
   AND id_Funcionario = :idFuncionario
